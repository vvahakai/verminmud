(define (filter proc lst)
  (if (null? lst)
		(list)
		(if (proc (car lst))
			 (cons (car lst) (filter proc (cdr lst)))
			 (filter proc (cdr lst)))))

(define (false? x)
  (eq? x #f))

(define (enum->list enum)
  (let ((acc (list))
		  (next (lambda ()
					 (if (.hasMoreElements enum)
						  (.nextElement enum)
						  #f))))

	 (do ((l (next) (next)))
		  ((not l) #f)
		
		(set! acc (cons l acc)))
	 (reverse acc)))

(define (for-iter fun iter)
  (let ((next (lambda ()
                (and (.hasNext iter)
                     (.next iter)))))
    (do ((datum (next) (next)))
        ((eq? #f datum) #f)

      (apply fun (list datum)))))

(define (string-join strings glue)
  (let ((sb (StringBuilder.)))
    (letrec ((iter (lambda (lst)
                     (.append sb (car lst))
                     (if (not (null? (cdr lst)))
                         (begin (.append sb glue)
                                (iter (cdr lst)))
                         (.toString sb)))))
	 
      (iter strings))))

(define (times num proc)
  (do ((i 0 (+ i 1)))
      ((= i num) #f)
    (proc i)))

	 
(define-macro (hook args . code)
  `(SchemeHook.
    (lambda ,args
      ,@code)))

;; From Ken Andersson (Jscheme user list)
(define (with what . kvs)
  (define (method? x) (instanceof x JavaMethod.class))
  (define (op kvs)
    (if (null? kvs) what
        (if (method? (car kvs))
            (args (car kvs) '() (cdr kvs))
            (error {expected operator, but got [(car kvs)]}))))
  (define (args method sofar kvs)
    (if (null? kvs)
        (begin (apply method what (reverse sofar))
               what)
        (if (method? (car kvs))
            (begin (apply method what (reverse sofar))
                   (op kvs))
            (args method (cons (car kvs) sofar) (cdr kvs)))))
  (op kvs))

;; From elf/utils.scm
(define (instanceof x c)
  (and (not (eq? x #null))
       (.isAssignableFrom c (.getClass x))))


(define-macro (aif condition true-clause . false-clause)
  `(let ((it ,condition))
     (if it
         ,true-clause
         ,(car false-clause))))

(define-macro (acond . clauses)
  (if (null? clauses)
      ()
      (let ((clause (car clauses)))
        `(aif ,(car clause)
              ,(cadr clause)
              (acond ,@(cdr clauses))))))


(define (index obj sequence)
  (letrec ((ind (lambda (seq pos)
                  (if (null? seq)
                      #f
                      (if (eq? (car seq) obj)
                          pos
                          (ind (cdr seq) (+ 1 pos)))))))
    (ind sequence 0)))

