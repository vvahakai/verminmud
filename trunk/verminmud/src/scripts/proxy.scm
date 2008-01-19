
;;;
;;; Implement interfaces dynamically in scheme.
;;; Uses the >=JDK 1.3 dynamic proxy class.
;;;
;;; 22.7.2003 Tatu Tarvainen / Council 4


(import "java.util.HashMap")
(import "java.lang.reflect.*")

(import "elf.SchemeInvocationHandler")

(define-macro (defproxy . forms)
  `(let ((handler (create-invocation-handler 
						 ,(extract-method-forms forms)
						 ,(extract-var-forms forms)
						 ,(extract-delegate-form forms)))
			(classes (create-class-list ',(map cadr (extract-implement-forms forms)))))
	  
	  (Proxy.newProxyInstance (.getClassLoader (car classes))
									  (list->array Class.class classes)
									  handler)))

(define-macro (create-invocation-handler method-forms var-forms delegate-form)
  `(SchemeInvocationHandler.
	 (let ((this #f)
			 ,(make-delegate-binding delegate-form)
			 ,@(make-method-bindings method-forms)
			 (methods (HashMap.)))
		
		(let (,@(make-var-bindings var-forms))
		  ,@(process-methods method-forms))
		
		(lambda (proxy method args)
		  
		  (set! this proxy)
		  (let* ((arglist (if (eq? args #null)
									 (list)
									 (array->list args)))
					(m (.get methods (string-append (.getName method) "/" (length arglist)))))
			 
			 (if (eq? m #null)

				  (if delegate
						(.invoke method delegate args)  ; call the host object
						(throw 'not-implemented))

				  (apply m arglist)))))))

(define (make-delegate-binding delegate-form)
  (list 'delegate (if (null? delegate-form)
						 #f
						 (cadr delegate-form))))

(define (make-method-bindings method-forms)
  (let ((methods (list)))
	 (filter (lambda (elt) (not (eq? #f elt)))
				(map (lambda (m)
						 (if (not (memq (cadr m) methods))
							  (begin
								 (set! methods (cons (cadr m) methods))
								 (list (cadr m) #f))))
					  method-forms))))


(define (extract-forms-beginning-with tag forms)
  (filter (lambda (elt) (not (eq? elt #f)))
			 (map (lambda (form)
					  (if (eq? (car form) tag)
							form
							#f)) forms)))

(define (extract-method-forms forms)
  (let ((method-forms (append
							  (extract-forms-beginning-with 'def forms)
							  (extract-reader/writer-from-vars
								(extract-var-forms forms)))))

	 ;; If no toString method is defined, add the default one
	 (if (not (memq 'toString (map cadr method-forms)))
		  (cons `(def toString () "<scheme-proxy>") method-forms)
		  method-forms)))

(define (extract-delegate-form forms)
  (car (extract-forms-beginning-with 'delegate forms)))

(define (extract-implement-forms forms)
  (extract-forms-beginning-with 'implement forms))
					
(define (extract-var-forms forms)
  (extract-forms-beginning-with 'var forms))

(define (extract-reader/writer-from-vars var-forms)
  (let ((lst (list)))
	 (for-each (lambda (var)
					 (let ((setter (assq 'writer (cddr var)))
							 (getter (assq 'reader (cddr var))))

						(if (not (eq? #f setter))
							 (set! lst 
									 (cons `(def ,(cadr setter) (-value-)
													 (set! ,(cadr var) -value-)) lst)))
						
						(if (not (eq? #f getter))
							 (set! lst 
									 (cons `(def ,(cadr getter) ()
													 ,(cadr var)) lst)))))
				  var-forms)
	 lst))

(define (make-var-bindings var-forms)
  (map (lambda (var)
			`(,(cadr var) ,(let ((code (assq 'initial (cddr var))))
								  (if code
										(cadr code)))))
		 var-forms))

(define (process-methods method-forms)
  (map process-method
		 method-forms))

(define (process-method m)
  `(let ((-m- (lambda ,(caddr m)
					 ,@(cdddr m)))
			(argc ,(length (caddr m))))
	  
	  ;; set a wrapper for the binding (for scheme to scheme calls)
	  (let ((old-method ,(cadr m)))
		 (set! ,(cadr m) 
				 (lambda ARGS
					(apply (if (= (length ARGS) argc)
								  -m-
								  old-method) ;; CHECK: this should refer to the previous binding
						  ARGS))))

	  ;; set the method to the hashmap (for InvocationHandler to scheme calls)
	  (.put methods (string-append ',(cadr m) "/" argc) -m-)))


(define (create-class-list class-names)
  (map class class-names))


(define (test)
  (let ((p (test-proxy)))
	 
	 (list (.compare p 2 7)
			 (.compare p 8 3)
			 (.compare p 6 6))))



(define (test-proxy)
  (defproxy 
	 
	 (def koe (x)
			(* x 2))

	 (def koe (x y)
			(* x y))

	 (def compare (x y) 
			(display {COMPARE: [x] & [y]}) (newline)
			(display {#### [x] * 2 = [(koe x)] #### [x] * [y] = [(koe x y)]}) (newline)
			(if (< x y) 
				 -1 
				 (if (> x y) 1 0)))

	 (def toString ()
			"Minä olen Vertailija!")
	 
	 (implement java.util.Comparator)))


(define (test-proxy1 count)
  (defproxy
	 (implement java.util.Observer)

	 (var count (initial count) (writer newcount))

	 ;(def toString () "<test-proxy1>")
	 (delegate (Object.))

	 (def update (obj arg)
			(display {The count is [count]}) (newline)
			(newcount (+ count 1)))))


;;;
;;; "Language" reference for dynamic objects:
;;;
;;; Forms allowed inside defproxy:
;;;
;;; (def name (args) body...)       Defines method of name with the given arguments.
;;;                                 args is a list of argument names. eg.
;;;  Example: (def says (who what)
;;;             (if (.containsKey attackers (.getName who))
;;;               (.notice who "You are going down!")))
;;;
;;; 
;;; (implement "fully.qualified.ClassName")  The resulting proxy will implement this interface
;;;                                          this form can be used multiple times to implement
;;;                                          more interfaces.
;;;
;;; (var name varopts...)           Defines a local variable in the proxy class.
;;;                                 Each varopt is a list of (tag value).
;;;                                 Allowed tags:
;;;                                   reader   specifies a 0-arg getter function
;;;                                   writer   specifies a 1-arg setter function
;;;                                   initial  specifies the initial value (form)
;;;
;;; (delegate form)                 Define the delegate object to which unimplemented calls
;;;                                 are delegated to. Form must return an object which
;;;                                 implements the same interfaces as this proxy.
;;;
;;; (include file)                  Include definitions from file.
