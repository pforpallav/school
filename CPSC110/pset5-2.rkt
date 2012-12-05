;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname pset5-2) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
;;; Problem Set 5
;;; Evan Louie
;;; m6d7

(require 2htdp/image)
(require 2htdp/universe)



;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;PROBLEM 1;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;

;;;
;;; CONSTANTS
;;;
(define BLANK (square 0 "solid" "white"))
(define TESTSQUARE (square 50 "solid" "blue"))

;;;
;;; DATA DEFINITIONS
;;;

;; A natural is one of:
;; - 0 
;; - (add1 Natural)
;; interp. natural numbers 0, (add1 0), (add1 (add1 0))...
(define n0 0)
(define n1 (add1 0))
(define n2 (add1 (add1 0)))
#;
(define (fn-for-natural n)
  (cond [(zero? n) (...)]
        [else
         (...n
          (fn-for-natural (sub1 n)))]))

;;;
;;; Functions
;;;

;; Natural Image -> Image
;; Consumes a natural n and an image i, produces image i, n times in a column
(check-expect (col 0 TESTSQUARE) BLANK)
(check-expect (col 1 TESTSQUARE) TESTSQUARE)
(check-expect (col 3 TESTSQUARE) (above TESTSQUARE
                                        TESTSQUARE
                                        TESTSQUARE))
(define (col n i)
  (cond [(zero? n) BLANK]
        [(positive? n) (above i (col (sub1 n) i))]))

;; Natural Image -> Image
;; Consumes a natural n and an image i, produces image i, n times in a column
(check-expect (row 0 TESTSQUARE) BLANK)
(check-expect (row 1 TESTSQUARE) TESTSQUARE)
(check-expect (row 3 TESTSQUARE) (beside TESTSQUARE
                                         TESTSQUARE
                                         TESTSQUARE))

(define (row n i)
  (cond [(zero? n) BLANK]
        [(positive? n) (beside i (row (sub1 n) i))]))






;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;PROBLEM 2;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;

;;;
;;; CONSTANTS
;;;

(define POLY3 (list (make-posn 0 0)
                    (make-posn 40 70)
                    (make-posn 67 34)))
(define POLY4 (list (make-posn 0 0)
                    (make-posn 25 76)
                    (make-posn 85 65)
                    (make-posn 76 34)))
(define POLY5 (list (make-posn 0 0)
                    (make-posn 86 47)
                    (make-posn 87 66)
                    (make-posn 46 16)
                    (make-posn 0 98)))
(define POLY6 (list (make-posn 0 0)
                    (make-posn 54 2)
                    (make-posn 25 56)
                    (make-posn 12 12)
                    (make-posn 86 23)
                    (make-posn 64 64)))
;;;
;;; DATA DEFINITIONS
;;;

;; A Poly is one of:
;; - (cons (make-posn Integer Integer)
;;         (cons (make-posn Integer Integer)
;;               (cons (make-posn Integer Integer)
;;                     empty)))
;; - (cons (make-posn Integer Integer) Poly)
;; interp. A polygon is a list of at least three points
#;
(define (fn-for-poly poly)
  (... (first poly)
       (rest poly)))

;; ListoOfPoly is Poly is one of:
;; - empty
;; - (cons Poly ListOfPoly)
;; interp. A list of Poly
#; 
(define (fn-for-lop lop)
  (cond [(empty? lop) (...)]
        [else
         (... (fn-for-poly (first lop))
              (fn-for-lop (rest lop)))]))


;;;
;;; FUNCTIONS
;;;

;; ListOfPoly -> ListOfPoly
;; Consumes a lop, produces a sorted lop (simplest first)
(check-expect (sort-lop (cons POLY4
                              (cons POLY3
                                    (cons POLY6
                                          (cons POLY5 empty)))))
              (cons POLY3
                    (cons POLY4
                          (cons POLY5
                                (cons POLY6 empty)))))

(define (sort-lop lop)
  (cond [(empty? lop) empty]
        [(cons? lop) (insert-lop (first lop) 
                                 (sort-lop (rest lop)))]))

;; Poly ListOfPoly -> ListOfPoly
;; Consumes a poly and sorted lop, produces a sorted lop
(check-expect (insert-lop POLY4 (cons POLY3
                                      (cons POLY4
                                            (cons POLY5
                                                  (cons POLY6 empty)))))
              (cons POLY3
                    (cons POLY4
                          (cons POLY4
                                (cons POLY5
                                      (cons POLY6 empty))))))

(define (insert-lop p lop)
  (cond [(empty? lop) (cons p lop)]
        [else (if (simpler? p (first lop))
                  (cons p lop)
                  (cons (first lop) (insert-lop p (rest lop))))]))

;; Poly Poly -> Boolean
;; Consumes two poly, produces true if first is simpler
(check-expect (simpler? POLY3 POLY5) true)
(check-expect (simpler? POLY4 POLY3) false)

(define (simpler? p lop)
  (<= (length p) (length lop)))


;; ListOfPoly -> Image
;; Consumes a lop, produces the rendered polygons in sorted order
(check-expect (arrange-polys empty) BLANK)
(check-expect (arrange-polys (cons POLY4
                                   (cons POLY3
                                         (cons POLY6
                                               (cons POLY5 empty)))))
              (beside (poly-image POLY3) 
                      (poly-image POLY4) 
                      (poly-image POLY5) 
                      (poly-image POLY6)))

(define (arrange-polys lop)
  (render-lop (sort-lop lop)))

;; ListOfPoly -> Image
;; Consumes a lop, renders the lop beside one another
(check-expect (render-lop empty) BLANK)
(check-expect (render-lop (cons POLY5
                                (cons POLY3
                                      (cons POLY4
                                            (cons POLY6 empty)))))
              (beside (poly-image POLY5) 
                      (poly-image POLY3) 
                      (poly-image POLY4) 
                      (poly-image POLY6)))

(define (render-lop lop)
  (cond [(empty? lop) BLANK]
        [else 
         (beside (poly-image (first lop))
                 (render-lop (rest lop)))]))

;; Poly -> Image
;; Consumes a poly, produces an image of the poly that is solid and red
(check-expect (poly-image POLY3) (polygon POLY3 "solid" "red"))
(check-expect (poly-image POLY5) (polygon POLY5 "solid" "red"))

(define (poly-image poly)
  (polygon poly "solid" "red"))
