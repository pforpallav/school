;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname lab2pset) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
;;
;; CPSC 110, 2010-2011 T1, Problem Set 2
;;
;; NAME: Evan Louie______________________
;;
;;

;;
;; Problem 1
;;

;; Direction is one of:
;; - "west"
;; - "north"
;; - "east"
;; - "south"
;; interp. as Direction currently facing
#;
(define (fn-for-Direction Direction)
  (cond [(string=? "north" Direction) (...)]
        [(string=? "west" Direction) (...)]
        [(string=? "east" Direction) (...)]
        [(string=? "south" Direction) (...)]))


;; left: Direction -> Direction
;; consumes a Direction, produces the next Direction after a counter-clockwise 90 degree turn
(check-expect (left "north") "west")
(check-expect (left "west") "south")
(check-expect (left "south") "east")
(check-expect (left "east") "north")

(define (left Direction)
  (cond [(string=? "north" Direction) "west"]
        [(string=? "west" Direction) "south"]
        [(string=? "south" Direction) "east"]
        [(string=? "east" Direction) "north"]))


;;
;; Problem 2
;;


;; NumericGrade is one of:
;;  - Integer[90, 100]   called "A"
;;  - Integer[80, 89]    called "B"
;;  - Integer[50, 79]    called "Pass"
;;  - Integer[0, 49]     called "Fail"
;; interp. numerical grades
#;
(define (fn-for-NumericGrade NumericGrade)
  (cond [(and (<= 90 NumericGrade) (>= 100 NumericGrade)) (...)]
        [(and (<= 80 NumericGrade) (> 90 NumericGrade)) (...)]
        [(and (<= 50 NumericGrade) (> 80 NumericGrade)) (...)]
        [(and (<= 0 NumericGrade) (> 50 NumericGrade)) (...)]))

;; There should be 9 examples/tests
;; There needs to be a test for all possible grades entered within the differing intervals (open and closed). 
;; This makes 4 tests for open intervals and 5 for closed:
;; (0-50)
;; (50-80)
;; (80-90)
;; (90-100)
;; [0]
;; [50]
;; [80]
;; [90]
;; [100]


;; numeric->letter: NumericGrade -> string
;; consumes a NumericGrade and produces the appropriate letter grade
(check-expect (numeric->letter 0) "Fail")
(check-expect (numeric->letter 50) "Pass")
(check-expect (numeric->letter 80) "B")
(check-expect (numeric->letter 90) "A")
(check-expect (numeric->letter 100) "A")
(check-expect (numeric->letter 25) "Fail")
(check-expect (numeric->letter 65) "Pass")
(check-expect (numeric->letter 85) "B")
(check-expect (numeric->letter 95) "A")

(define (numeric->letter NumericGrade)
  (cond [(and (<= 90 NumericGrade) (>= 100 NumericGrade)) "A"]
        [(and (<= 80 NumericGrade) (> 90 NumericGrade)) "B"]
        [(and (<= 50 NumericGrade) (> 80 NumericGrade)) "Pass"]
        [(and (<= 0 NumericGrade) (> 50 NumericGrade)) "Fail"]))