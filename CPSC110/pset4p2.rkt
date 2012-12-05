;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname pset4p2) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
(require 2htdp/universe)
(require 2htdp/image)

;;;;
;;;; DATA DEFINITIONS
;;;;

;; ListOfString is one of:
;; - empty
;; - (cons String ListOfString)
;; interp. a list of strings

(define los1 empty)
(define los2 (cons "ball" empty))
(define los3 (cons "Firebolt" (cons "portkey" empty)))

#;
(define (fn-for-los los)
  (cond [(empty? los) (...)]
        [else
         (... (first los)
              (fn-for-los (rest los)))]))

;;;;
;;;; FUNCTIONS
;;;;

;; ListOfString -> ListOfStrings
;; Consumes of listofstrings, Produces new ListOfString with "accio " appended to all entries
(check-expect (accio-all empty) empty)
(check-expect (accio-all (cons "test" empty)) (cons "accio test" empty))
(check-expect (accio-all (cons "test1" (cons "test2" empty))) 
              (cons "accio test1" (cons "accio test2" empty)))

(define (accio-all los)
  (cond [(empty? los) empty]
        [else
         (cons (string-append "accio " (first los))
               (accio-all (rest los)))]))

