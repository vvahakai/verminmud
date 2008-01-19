;;;
;;; Implementation of SRFI-26 for Jscheme
;;; Author: Tatu Tarvainen (ttarvain@mail.student.oulu.fi)
;;; Date: 3.july.2004
;;;
;;;;;;;
;;; Copyright (c) 2004 Tatu Tarvainen
;;; This system is licensed under the following 
;;; zlib/libpng open-source license.
;;;
;;; This software is provided 'as-is', without any express or
;;; implied warranty.
;;;
;;; In no event will the authors be held liable for any damages
;;; arising from the use of this software.
;;;
;;; Permission is granted to anyone to use this software for any
;;; purpose, including commercial applications, and to alter it
;;; and redistribute it freely, subject to the following
;;; restrictions:
;;;
;;; 1. The origin of this software must not be misrepresented; you
;;;    must not claim that you wrote the original software. If you
;;;    use this software in a product, an acknowledgment in the
;;;    product documentation would be appreciated but is not
;;;    required.
;;;
;;; 2. Altered source versions must be plainly marked as such, and
;;;    must not be misrepresented as being the original software.
;;; 
;;; 3. This notice may not be removed or altered from any source
;;;    distribution.
;;;;;;;
;;;
;;;
;;; NOTES:
;;; Implements macros `cut´ and `cute´ as defined
;;; by SRFI-26 (http://srfi.schemers.org/srfi-26/).
;;;
;;; This implementation is not hygienic because it introduces
;;; new symbols to the lexical environment. The symbols have
;;; a prefix of `%cut-slot-´ or `%cute-expr-´ to minimize the
;;; risk of accidental capture.
;;;

(define-macro (cut proc . slots)
  (let* ((slot-info (count-slots proc slots))
			(slot-count (car slot-info))
			(rest? (cdr slot-info)))
	 `(lambda ,(gen-lambda-args slot-count rest?)
		 ,(gen-lambda-body proc slots rest? #f))))

(define-macro (cute proc . slots)
  (let* ((slot-info (count-slots proc slots))
			(slot-count (car slot-info))
			(rest? (cdr slot-info)))
	 `(let ,(gen-cute-exprs proc slots)
		 (lambda ,(gen-lambda-args slot-count rest?)
			,(gen-lambda-body proc slots rest? #t)))))


(define (gen-arg suffix)
  (string->symbol (string-append "%cut-slot-" suffix)))

(define (gen-expr suffix)
  (string->symbol (string-append "%cute-expr-" suffix)))

(define (gen-lambda-args slot-count rest?)
  (let ((args ()))
	 (do ((i 0 (+ 1 i)))
		  ((= i slot-count) (append
									(reverse args)
									(if rest? (gen-arg "rest") ())))

		(set! args (cons (gen-arg i) args)))))

(define (generator fun)
  (let ((i 0))
	 (lambda ()
		(let ((result (fun i)))
		  (set! i (+ i 1))
		  result))))

(define (gen-lambda-body proc slots rest? cute?)
  (let ((next-arg (generator gen-arg))
		  (next-expr (generator gen-expr)))

	 (letrec ((traverse (lambda (acc slots)
								 (if (or (null? slots)
											(rest-slot? (car slots)))
									  (reverse acc)
									  (traverse (cons (if (slot? (car slots))
																 (next-arg)
																 (if cute?
																	  (next-expr)
																	  (car slots)))
															acc)
													(cdr slots))))))
		(let* ((func (if (slot? proc)
							  (next-arg)
							  (if cute?
									(next-expr)
									proc)))
				 (args (traverse () slots)))
		  
		  `(apply ,func (append (list ,@args)
										,(if rest?
											  (gen-arg "rest")
											  ())))))))



(define (gen-cute-exprs proc slots)
  (let* ((exprs ())
			(next-expr (generator gen-expr))
			(gen-expr (lambda (expr)
							(set! exprs (cons `(,(next-expr) ,expr) exprs)))))

	 (if (not (or (slot? proc) (rest-slot? proc)))
		  (gen-expr proc))
	 (for-each (lambda (expr)
					 (if (not (or (slot? expr) (rest-slot? expr)))
						  (gen-expr expr)))
				  slots)
	 exprs))

  
(define (slot? slot-or-expr)
  (eq? '<> slot-or-expr))

(define (rest-slot? slot-or-expr)
  (eq? '<...> slot-or-expr))

;;; Returns a pair with slot count and rest args as (slot-count . rest-args?)
(define (count-slots proc slots)
  (let ((slot-count (if (slot? proc) 1 0))
		  (rest-args #f))
	 (letrec ((traverse (lambda (slots)
								 (if (null? slots)
									  #f
									  (if (rest-slot? (car slots))
											(set! rest-args #t)
											(begin 
											  (if (slot? (car slots)) 
													(set! slot-count (+ 1 slot-count)))
											  (traverse (cdr slots))))))))
		(traverse slots))
	 (cons slot-count rest-args)))


;;; Testing code

(define (cut-tests)
  (do-test (map (cut * <> 2) (list 1 2 3 4)) (list 2 4 6 8))
  
  (do-test ((cut list 1 <> 3 <...>) 2 4 5) (list 1 2 3 4 5))
  
  (do-test (map (cut <> 8 2) (list * / + -)) (list 16 4 10 6))
  
  ;; with cute the non-slot expression are evaluated when the function
  ;; is constructed, so the time will not change
  (let* ((f (cute list (System.currentTimeMillis) <...>))
			(l1 (f 1))
			(l2 (begin
					(Thread.sleep 50l)
					(f 2))))
	 (assert (equal? (car l1) (car l2)))))


