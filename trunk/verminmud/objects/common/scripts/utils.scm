
(use-module "elf/future.scm")

(use-module "common/scripts/cut.scm" 'import-macros)

(define (filter p xs)
  (if (null? xs) xs
      (if (p (car xs)) (cons (car xs) (filter p (cdr xs)))
	  (filter p (cdr xs)))))

(define (escape-sql evil)
  (.replace evil "'" "\\'"))

(define (table headers rows)
  (let ((t (org.vermin.util.Table.)))
    (for-each (lambda (h)
		(.addHeader t (car h) (cadr h) (caddr h)))
	      headers)
    (for-each (lambda (r)
		(.addRow t)
		(for-each (cut .addColumn t <>) r))
	      rows)
    t))
			  
;;; @procedure match-to-groups
;;; @param pattern the pattern to match against, must be a String or a java.util.regex.Pattern
;;; @param string the string to match
;;; @description Returns the matched regular expression groups or #f, if no match
;;; @return a list of matched subgroup strings, or #f
(define (match-to-groups pattern string)
  (let* ((p (or (and (string? pattern)
		     (java.util.regex.Pattern.compile pattern))
		pattern))
	 (matcher (.matcher p string)))
    (if (not (.matches matcher))
	#f
	(letrec ((accum (lambda (i acc)
			  (if (= i 0) ; skip the first (while match) group
			      acc
			      (accum (- i 1) (cons (.group matcher i) acc))))))
	  (accum (.groupCount matcher) (list))))))

		 


;;; @procedure call-with-string-output
;;; @param thunk a procedure of one argument
;;; @description the parameter procedure is called with an output port
;;;              that captures the output into a string buffer
;;; @return the captured output as a string
(define (call-with-string-output thunk)
  (let ((out (java.io.StringWriter.)))
    (thunk (java.io.PrintWriter. out))
    (.toString out)))

;;; @procedure split
;;; @param regex a regular expression
;;; @param string the string to split
;;; @return a list of tokens
;;; @description split the given string with the given regular expression
;;;              empty tokens (containing only whitespace) are removed
(define (split regex string)
  (filter (lambda (x)
	    (> (string-length (.trim x)) 0))
	  (array->list (.split string regex))))

;;; @procedure run-process
;;; @param working-dir the working directory or #null for default
;;; @param environment an alist of environment variables (name value)
;;; @param output-to procedure to send output to (line by line)
;;; @param cmd
;;; @param args 
;;; @return #t on success, #f on failure
;;; @description Run an external process.
(define (run-process working-dir environment output-to cmd . args)
  (let*	((builder (ProcessBuilder. (list->array String.class 
						(append (list cmd)
							args))))
	 (env (.environment builder))
	 (dummy (for-each (lambda (e)
			    (.put env (car e) (cadr e)))
			  environment))

	 (process (.start (if (not (eq? #null working-dir))
			       (.directory builder (if (string? working-dir)
						       (java.io.File. working-dir)
						       working-dir))
			       builder)))
	 (wait (future (.waitFor process)))
	 (reader (java.io.BufferedReader. (java.io.InputStreamReader. (.getInputStream process)))))

    (do ((line (.readLine reader) (.readLine reader)))
	((or (determined? wait) 
	     (eq? #null line)) (= 0 (.waitFor process)))

      (output-to line))))



