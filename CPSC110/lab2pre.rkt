;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname lab2pre) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
;; String String -> Nat
;; Compute the combined length of two strings
(check-expect (strings-length "hello" "world") 10)
;; stringlength: name is not defined, not a parameter, and not a primitive name
;;;; missing dash in "stringlength s2"
;; string-length: expects argument of type <string>; given 110
;;;; string-length accepts a single string as an argument; so it must be nested above another function in order to deal with non strings.
(define (strings-length s1 s2)
  (if (and 
       (string? s1) (string? s2)) 
      (+(string-length s1) 
        (string-length s2)) 
      (if (and
           (string? s1) (number? s2))
          (+ (string-length s1) 
             (string-length (number->string s2)))
          (if (and
               (number? s1) (string? s2))
              (+ (string-length (number->string s1))
                 (string-length s2))
              "either s1 or s2 is not a string or a number"))))

;; Integer -> Integer
;; Add 2 to the given Integer
(check-expect (add2 5) 7)
;; read: unexpected `)'
;;; too many closing brackets
(define (add2 int)
  (+ 2 int))


(define COURSE-CODE "CPSC")

;; Compute the string length of COURSE-CODE and 110
(strings-length COURSE-CODE 110)

;; Extract the first 2 characters from COURSE-CODE
;; substring: ending index 10 out of range [0, 4] for string: "CPSC"
;;;; substring cannot run when its arguments are not within the given parameters; CPSC is only 4 characters, we are asking for substring to evalutate up to 10
(substring COURSE-CODE 0 2)