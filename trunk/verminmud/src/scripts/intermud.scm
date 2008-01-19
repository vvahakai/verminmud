;;; Implement Intermud protocols

(use-module "elf/util.scm")
(use-module "elf/iterate.scm")

;; LPC serialization

;;;;;;;;;;;;;
;; Reading

(define (lpc->java in error)
  (let ((ch (peek-char in)))
    (case ch
      ((#\() (read-lpc-collection in error))
      ((#\") (read-lpc-string in error))
      ((#\-) (begin (read-char in) (* -1 (read-lpc-integer in 0 error))))
      ((#\0 #\1 #\2 #\3 #\4 #\5 #\6 #\7 #\8 #\9) (read-lpc-integer in 0 error)))))

(define (read-lpc-collection in error)
  (let* ((open-brace (read-or-fail in #\( error)) ;; discard '('
	 (ch (read-char in))
	 (value (cond
		 ((char=? ch #\[) (read-lpc-dictionary in (java.util.HashMap.) error))
		 ((char=? ch #\{) (read-lpc-list in (java.util.ArrayList.) error))
		 (else (error "Expected [ or { after open brace."))))
	 (close-brace (read-or-fail in #\) error))) ;; discard ')'
    value))	

(define (read-or-fail in expected-char error)
  (let ((ch (read-char in)))
    (if (not (char=? ch expected-char))
	(error {Expected '[expected-char]', but got '[ch]'.})
	#t)))

(define (read-lpc-dictionary in accum error)
  (if (char=? (peek-char in) #\])
      (begin (read-char in)
	     accum)
      (let* ((key (lpc->java in error))
	     (sep (read-or-fail in #\: error))
	     (value (lpc->java in error))
	     (ch (read-char in)))
	(.put accum key value)
	(cond 
	 ((char=? ch #\,)
	  (read-lpc-dictionary in accum error))
	 ((char=? ch #\])
	  accum)
	 (else (error "Expected , or ] after dictionary pair."))))))

(define (read-lpc-list in accum error)
  (if (char=? (peek-char in) #\})
      (begin (read-char in)
	     accum)
      (let* ((value (lpc->java in error))
	     (ch (read-char in)))
	(.add accum value)
	(cond
	 ((char=? ch #\}) accum)
	 ((char=? ch #\,) (read-lpc-list in accum error))
	 (else (error "Expected } or , after list value."))))))

(define (read-lpc-integer in accum error)
  (let* ((ch (read-char in))
	 (new-value (+ (* accum 10) (Character.digit ch 10))))
    (if (char-numeric? (peek-char in))
	(read-lpc-integer in new-value error)
	new-value)))
	
(define (read-lpc-string in error)
  (read-char in) ;; discard beginning '"'
  (let ((accum (StringBuffer.)))
    (letrec ((inner-read (lambda ()
			   (let ((ch (read-char in)))
			     (cond 
			      ((char=? ch #\") (.toString accum))
			      ((char=? ch #\\) 
			       (begin (.append accum (read-char in))
				      (inner-read)))
			      (else (begin
				      (.append accum ch)
				      (inner-read))))))))
      (if (char=? (peek-char in) #\")
	  (begin (read-char in)
		 "")
	  (inner-read)))))

(define (test-read-string str)
  (call-with-current-continuation 
   (lambda (error)
     (lpc->java (jsint.InputPort. (java.io.StringReader. str)) error))))

(define (test-read-input)
  (read-char)
  (call-with-current-continuation
   (lambda (error)
     (lpc->java (current-input-port) error))))


;;;;;;;;;;;;;;
;; Writing

(define (java->lpc obj out error)
  (cond
   ((string? obj) (write-lpc-string obj out error))
   ((integer? obj) (write-lpc-integer obj out error))
   ((instanceof obj java.util.Map.class) (write-lpc-dictionary obj out error))
   ((or (list? obj) 
	(instanceof obj java.util.Collection.class)) (write-lpc-list obj out error))
   (else (error {Unable to write object of class: [(.getClass obj)]}))))

(define (write-lpc-string obj out error) (write obj out))
(define (write-lpc-integer obj out error) (write obj out))
(define (write-lpc-dictionary obj out error)
  (display "([" out)
  (iterate (.keySet obj)
	   (lambda (key)
	     (java->lpc key out error)
	     (display ":" out)
	     (java->lpc (.get obj key) out error)
	     (display "," out)))
  (display "])" out))
(define (write-lpc-list obj out error)
  (display "({" out)
  (iterate obj
	   (lambda (value)
	     (java->lpc value out error)
	     (display "," out)))
  (display "})" out))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Higher level communication

(define *router-name* "*gjs")
(define *router-host* "us-1.i3.intermud.org")
(define *router-port* 9000)
(define *router-password* 0) ;; Password to use (0 means new mud and server gives us a new password)

(define *router-connection-socket* #f)
(define *router-connection-in* #f)
(define *router-connection-out* #f)

;; mud registration info
(define *mud-player-port* 232)
(define *mud-open-status* "beta testing")
(define *mud-admin-email* "tadex@verminmud.org")

(define *mud-name* "VerminMUD Testing")

;; Open a TCP connection to the router
(define (connect-to-router)
  (let ((s (java.net.Socket. *router-host* *router-port*)))
    (set! *router-connection-socket* s)
    (set! *router-connection-in* (jsint.InputPort. (java.io.InputStreamReader. (.getInputStream s))))
    (set! *router-connection-out* (java.io.PrintWriter. (.getOutputStream s)))))

(define (intermud-send packet time-to-live . args)
  (call-with-current-continuation 
   (lambda (error)
     (display "INTERMUD SEND: ")
     (java->lpc (cons packet (cons time-to-live args)) (current-output-port)
		error)
     (newline)
     
     (java->lpc 
      (cons packet (cons time-to-live args))
      *router-connection-out*
      error)
     (.flush *router-connection-out*)
     #t)))

(define (intermud-connect)
  (connect-to-router)
  (start-listener)
  (intermud-send "startup-req-3"
		 5
		 *mud-name*
		 0
		 *router-name*
		 0
		 
		 *router-password* 
		 0 0 ;; old_mudlist_id and old_chanlist_id ???
		 
		 *mud-player-port*

		 ;; We don't currently provide out-of-band services, so these are zero
		 0 ;; *imud-tcp-port*
		 0 ;; *imud-udp-port*

		 "Vermin" ;; mudlib
		 "Vermin" ;; mudlib base
		 "Vermin" ;; driver
		 "Vermin" ;; mud-type
		 
		 *mud-open-status*
		 *mud-admin-email*
		
		 ;; Services mapping
		 (dictionary "tell" 1)
			     
		 ;; Other data
		 0))

(define (start-listener)
  (.start (Thread. (lambda () (intermud-listen-loop)))))

(define (intermud-listen-loop)
  (let ((read-packet (lambda ()
		       (let* ((p #f)
			      (ret (call-with-current-continuation 
				    (lambda (error)
				      (set! p (lpc->java *router-connection-in* error))))))
			 (if (not (eq? p #f)) p 
			     (begin
			       (display {Error reading input: [ret]}) 
			       (newline)
			       #f))))))

    (do ((packet (read-packet) (read-packet)))
	((eq? packet #f) #f)

      (display "GOT PACKET: ")
      (java->lpc packet (current-output-port) (lambda (X) (display {PACKET CORRUPT: [X]}) (newline))) 
      (newline)
      ;; DISPATCH TO A HANDLER FUNCTION
      )))


(define (dictionary . args)
  (let ((hashmap (java.util.HashMap.)))
    (letrec ((dic (lambda (args)
		    (if (null? args)
			hashmap
			(begin
			  (.put hashmap (car args) (cadr args))
			  (dic (cddr args)))))))
      (dic args))))

;;;;;;;;;;;;;;;;;;;;
;; Service Methods

(define-macro (define-intermud-function name . args)
  `(define (,name ,args)
     (intermud-send ,(symbol->string name)
		    5 ;; TTL is always five
		    *mud-name*
		    ,@args)))

(define-intermud-function tell originator-username target-mud target-user originator-visible-name message)


  
  
		    