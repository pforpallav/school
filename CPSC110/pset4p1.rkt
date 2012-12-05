;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname pset4p1) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
(require 2htdp/universe)
(require 2htdp/image)

;;;;
;;;; DATA DEFINITIONS
;;;;

;; ListofImages is one of:
;; - empty
;; - (cons Image (listofImages))
;; interp. a list of images

(define LOI1 empty)
(define LOI2 (cons (rectangle 1 1 "solid" "black") empty))

#;
(define (fn-for-loi loi)
  (cond [(empty? loi) (...)]                
        [else (... (first loi)
                   (fn-for-loi (rest loi)))]))



;;;;
;;;; FUNCTIONS
;;;;

;; Image -> Boolean
;; produce true if image is tall (height > width)
(check-expect (tall? (rectangle 1 2 "solid" "red")) true)
(check-expect (tall? (rectangle 2 2 "solid" "red")) false)
(check-expect (tall? (rectangle 2 1 "solid" "red")) false)

(define (tall? img)
  (> (image-height img)
     (image-width img)))


;; ListofImages -> Boolean
;; Consumes a ListofImages, Produces true if all entries are tall, otherwise false
(check-expect (all-tall? empty) true)
(check-expect (all-tall? (cons (rectangle 1 1 "solid" "black") empty)) false)
(check-expect (all-tall? (cons (rectangle 1 2 "solid" "black") empty)) true)
(check-expect (all-tall? (cons (rectangle 1 2 "solid" "black") 
                               (cons (rectangle 1 2 "solid" "black") empty))) true)
(check-expect (all-tall? (cons (rectangle 1 2 "solid" "black") 
                               (cons (rectangle 1 2 "solid" "black") 
                                     (cons (rectangle 1 1 "solid" "black") empty)))) false)

(define (all-tall? loi)
  (cond [(empty? loi) true]             
        [else
         (cond [(tall? (first loi)) 
                (all-tall? (rest loi))]
               [else false])]))
               
               
               
               
               
               
               