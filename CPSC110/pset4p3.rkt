;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname pset4p3) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
(require 2htdp/universe)
(require 2htdp/image)

;;;;
;;;; DATA DEFINITIONS
;;;;

;; Path is one of:
;; - (cons (make-posn Integer Integer) empty)
;; - (cons (make-posn Integer Integer) Path)
;; interp. the points along the path; a path cannot be empty, it ends at the last point

(define P1 (cons (make-posn 2 0) empty))
(define P2 (cons (make-posn 2 3) P1))
(define P3 (cons (make-posn 5 3) P2))

#;
(define (fn-for-path path)
  (cond [(empty? (rest path)) ....]
        [else
         (... (first path)
              (fn-for-path (rest path)))]))


;;;;
;;;; FUNCTIONS
;;;;

;; Posn Posn -> Number
;; returns the cartesian distance between two posns
(check-expect (distance (make-posn 0 0) (make-posn 0 2)) 2)
(check-expect (distance (make-posn 0 0) (make-posn 3 0)) 3)
(check-expect (distance (make-posn 0 0) (make-posn 3 4)) 5)

(define (distance p1 p2)
  (sqrt (+ (sqr (- (posn-x p1) (posn-x p2)))
           (sqr (- (posn-y p1) (posn-y p2))))))

;; Path -> Number
;; consumes a path, produces the sum of the distances between points
(check-expect (path-distance P1) 0)
(check-expect (path-distance P2) 3)
(check-expect (path-distance P3) 6)
(check-expect (path-distance (cons (make-posn 0 1)
                                   (cons (make-posn 0 2) empty))) 1)
(check-expect (path-distance (cons (make-posn 0 1)
                                   (cons (make-posn 0 1) empty))) 0)

(define (path-distance path)
  (cond [(empty? (rest path)) 0]
        [else
         (+ (distance (first path) 
                      (first (rest path)))
            (path-distance (rest path)))]))

