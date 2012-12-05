;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname lab3pset) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
;;
;; CPSC 110, 2010-2011 T1, Problem Set 3
;;
;; NAME: Evan Louie______________________
;;
;;

(require 2htdp/universe)
(require 2htdp/image)

;;
;; Constants:
;;

(define text-size 14)

(define width
  (* text-size 10))

(define height
  (* text-size 12))

(define MTS
  (empty-scene width height))

;;
;; Data definitions:
;;

;; Colour is one of:
;; - "red"
;; - "green"
;; - "blue"
#;
(define (fn-for-colour Colour)
  (cond [(string=? "red" Colour) (...)]
        [(string=? "blue" Colour) (...)]
        [(string=? "green" Colour) (...)]))

;; Count is integers [0,10]

(define-struct cnc (count colour))
;; cnc is (make-cnc Number String)
;; interp. the current Count & Colour of the countdown/"Happy New Years"
(define cnc1 (make-cnc 5 "blue"))
(define cnc2 (make-cnc 0 "green"))
(define cnc3 (make-cnc 10 "red"))
(define start (make-cnc 10 "red")) ;; starting state; to be used with main function
#;
(define (fn-for-cnc cnc)
  (... (cnc-count cnc)
       (cnc-colour cnc)))

;;
;; Functions:
;;

;; WorldState -> WorldState
;; no tests for main functions
(define (main t)       ;; to start function, use (main start)
  (big-bang t
            (on-tick tick-handler 1)
            (to-draw render)))

;; Count & Colour -> Count & Colour
;; Consumes a Count & Colour, produces Count minus one -or zero if already zero- and cycles the Colour 
(check-expect (tick-handler (make-cnc 0 "red")) (make-cnc 0 "blue"))
(check-expect (tick-handler (make-cnc 10 "blue")) (make-cnc 9 "green"))
(check-expect (tick-handler (make-cnc 5 "green")) (make-cnc 4 "red"))

(define (tick-handler cnc)
  (make-cnc (if (= (cnc-count cnc) 0)
                (cnc-count cnc)
                (sub1 (cnc-count cnc)))
            (cond [(string=? "red" (cnc-colour cnc)) "blue"]
                  [(string=? "blue" (cnc-colour cnc)) "green"]
                  [(string=? "green" (cnc-colour cnc)) "red"])))


;; Count & Colour -> Image
;; Consumes a Count & Colour, produces/renders the image
(check-expect (render (make-cnc 0 "red")) (place-image (text "Happy New Year" text-size (cnc-colour (make-cnc 0 "red"))) (/ width 2) (- height (* text-size (+ 1 (cnc-count (make-cnc 0 "red"))))) MTS))
(check-expect (render (make-cnc 5 "blue")) (place-image (text (number->string (cnc-count (make-cnc 5 "blue"))) text-size (cnc-colour (make-cnc 5 "blue"))) (/ width 2) (- height (* text-size (+ 1 (cnc-count (make-cnc 5 "blue"))))) MTS))
(check-expect (render (make-cnc 10 "green")) (place-image (text (number->string (cnc-count (make-cnc 10 "green"))) text-size (cnc-colour (make-cnc 10 "green"))) (/ width 2) (- height (* text-size (+ 1 (cnc-count (make-cnc 10 "green"))))) MTS))

(define (render cnc)
  (place-image (text (if (= (cnc-count cnc) 0)
                         "Happy New Year"
                         (number->string (cnc-count cnc)))
                     text-size
                     (cnc-colour cnc))
               (/ width 2)
               (- height (* text-size (+ 1 (cnc-count cnc))))
               MTS))


(main start)
