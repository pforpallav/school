;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname pset1) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
;;
;; CPSC 110, 2010-2011 T1, Problem Set 1
;;
;; NAME: Evan Louie___________________
;;
;;


(require 2htdp/universe)
(require 2htdp/image)

;;
;; Problem 1
;;
(* 3 5 7)
(* (* 3 5) 7)



;;
;; Problem 2
;;

(beside (above (square 20 'solid 'red) (square 20 'solid 'blue))
        (above (square 20 'solid 'blue) (square 20 'solid 'red)))


;;
;; Problem 3
;;
(string-append "accio" " " "firebolt")




;;
;; Problem 4
;;
(define (accio x)
  (if (string? x) (string-append "accio " x) "not a string"))




