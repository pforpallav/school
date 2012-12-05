;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname lab4pre) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
;;
;; CPSC 110, Fall 2010
;; Practice Problems for Midterm 1
;;
;; These problems are intended to provide you with practice exercises for
;; the first midterm. Additional practice problems covering material from 
;; week 4 will be distributed at the end of that week.
;;
;; Regular lab sections during week 4 will be devoted to working through
;; these practice problems. TAs will be able to give you advice on working
;; the problems.  TAs in the DLC will also be able to help with these
;; practice problems.
;;
;; NOTE: There are FAR MORE PROBLEMS HERE THAN WILL APPEAR ON AN ACTUAL
;;       MIDTERM. We are giving you extra problems to help you practice.
;;


;; ---------
;; Section 1
;; 
;;


;; What is the value of:
(+ 2 (* 3 5))
;;;; 17

;; What is the value of:
(string-append "Roberto" " " "Luongo")
;;;; "Roberto Luongo"

;; What is the value of:
(< 3 4)
;;;; "true"

;; What is the value of:
(if (< 1 3)
    "dog"
    "cat")
;;;; "dog"

;; Given
(define SPEED 10)

;; What is the value of

(* SPEED 2)
;;;; 20

;; Given
(define (prod x y)
  (* x y))

;; what is the value of

(prod 3 7)
;;;; 21


;; Given

(define (choose a b)
  (if (< a b)
      a
      b))

;; what is the value of

(choose 5 3)
;;;; 3


;; Given
(define-struct pkg (a b c))

(define P1 (make-pkg 3 4 5))

;; what is the value of
(pkg-b P1)
;;;; 4

;; ---------
;; Section 2
;; 

;; Consider this define-struct

(define-struct dims (width height length))


;; What is the name of the constructor it defines?
;;;; define-struct
;; What is the name of the predicate it defines?
;;;; dims
;; What are the names of all the selectors it defines?
;;;; width, height and length

;; ---------
;; Problem 3
;;
;; Kinds of Data definitions
;;

;; In this problem you will be given several small fragments of
;; a problem description.  Each describes some information in a
;; problem domain that must be respresented using data in a program.
;;
;; In each case say whether the data definition you would use is:
;;    unrestricted atomic    (Integer, String, Number etc.)
;;    interval
;;    enumeration
;;    itemization of intervals
;;    compound
;;
;; CHOOSE ONLY ONE, THE MOST SPECIFIC 
;; 
;; You may find it helpful in each case to assume you are working
;; on a typical world program, in which typical constants like WIDTH,
;; HEIGHT, etc. have been defined.
;;
;; You may make any other reasonable assumptions you wish, but if they
;; are essential to the correctness of your answer write your assumption
;; down.
;;

;; 
;; the x coordinate of the cat
;;
;;;; Unrestricted Atomic

;;
;; the height of the rocket
;;
;;;; Unrestricted Atomic
;;
;; the current point in a countdown from 10 to 0
;;

;;
;; whether the flashing light is yellow or dark
;;

;; 
;; the x and y coordinate of a plane
;;


;; 
;; the color and height of a sparkling firework
;; (where sparkling means that its color keeps changing)
;; 



;; ---------
;; Problem 4
;;
;; Designing Data Definitions
;;

;; In this problem you will design complete data definitions for several different
;; kinds of program domain information that must be represented as data in
;; a program. Assume you have access to the HtDDD web page. You should
;; consider all of these to be SEPARATE PROGRAMS.
;;
;; For some of these questions you may find it helpful in each case to
;; assume you are working on a typical world program, in which typical
;; constants like WIDTH, HEIGHT, etc. have been defined.
;;
;; You may make any other reasonable assumptions you wish, but if they
;; are essential to the correctness of your answer write you assumption
;; down.


;; the x coordinate of a flying saucer
;;;; xcor is a number
;;;; interp. x coordinate of the saucer
(define start-xposn 0)
(define end-xposn 10)
#;
(define (fn-for-xcor)
  (... xcor))

;; the ice cream flavor, one of chocolate, vanilla or strawberry
;;;; iceflav is one of;
;;;; - "chocolate"
;;;; - "vanilla"
;;;; - "strawberry"
;;;; interp. the flavor of the ice cream
#;
(define (fn-for-iceflav iceflav)
  (cond [(string=? "chocolate" iceflav) (...)]
        [(string=? "vanilla" iceflav) (...)]
        [(string=? "strawberry" iceflav) (...)]))





;; a score, which is an integer ranging from 0 to 5 inclusive
;;;; score is Integers [0,5]
;;;; interp. the current score of a game
(define score0 0)
(define score1 3)
(define score2 5)
#;
(define (fn-for-score score)
  (... score))




;; a reading, which an integer in one of the ranges [0, 3)  [3, 7) [7, 9]
;;;; reading is one of:
;;;; - Integers [0,3)
;;;; - Integers [3,7)
;;;; - Integers [7,9]
;;;; interp.  the reading 
#;
(define (fn-for-reading read)
  (cond [{and (>= read 0) (< read 3)} (...)]
        [{and (>= read 3) (< read 7)} (...)]
        [{and (>= read 7) (<= read 9)} (...)]))



;; a person, with a first name and a last name
(define-struct person (first last))
;; person is (make-person String String)
;; interp. a person with first name first and last name last
(define me (make-person "Evan" "Louie"))
#;
(define (fn-for-person person)
  (... (person-first person)
       (person-last person)))




;; a plane, with a altitude, compass direction and speed
;; Note: compass direction means an integer in [0, 359)
(define-struct plane (alt compass speed))
;; compass is integers: [0,359)
;; plane is (make-plane Number Number Number)
;; interp. a plane at altitude alt, compass direction compass and speed speed
(define plane1 (make-plane 300 300 300))
#;
(define (fn-for-plane plane)
  (... (plane-alt plane)
       (plane-compass plane)
       (plane-speed plan)))



;; ---------
;; Problem 5

;; Given this data definition:

;; Weight is Number
;; interp. a person's weight in kilos
(define W1 52)
(define W2 72)
#;
(define (fn-for-w w)
  (... w))


;; Design a function called half-weight that consumes a 
;; Weight and produces another Weight that is half as much.
;; Please explictly show your stub, as well as your template.

;; Weight -> Weight
;; Consumes a Weight, produces half the Weight
(check-expect (halfweight 150) 75)
(check-expect (halfweight 100) 50)
#;
(define (halfweight weight) ; this is the stub
  (... weight ...))

(define (halfweight weight)
  (/ weight 2))


;; ----
;; Given this data definition:


;; Name is String
;; interp. a dog's name
(define N1 "spot")
(define N2 "rover")
#;
(define (fn-for-name n)
  (... n))

;; Design a function called long-name? that consumes a Name and produces
;; true if the length of the name is longer than 8 and false otherwise.
;; Please explictly show your stub, as well as your template.
;;
;; You may want to use the BSL string-length function, which has the signature
;; String -> Natural, and produces the length of the string it is provided.

;; Name -> Boolean
;; consumes a Name and produces true if under 8 characters, false otherwise
(check-expect (longname "test") "true")
(check-expect (longname "testtest") "true")
(check-expect (longname "testtesttest") "false")
#;
(define (longname name) ; This is the stub
  (... name ...))

(define (longname name)
  (cond [(and (>= (string-length name) 0) 
              (<= (string-length name) 8))
         "true"]
        [else "false"]))




;; ----
;; Given this data definition:

(define-struct fw (y fuse))
;; Firework is (make-fw Integer Natural)
;; interp. a firework, y is height, fuse is number of ticks before it explodes
(define FW1 (make-fw 400 99))
(define FW2 (make-fw 47 1))   ;this one is about to explode

(define (fn-for-fw f)
  (... (fw-y f) (fw-fuse f)))


;; Design a function named exploded? that consumes a Firework and produces true if the
;; its fuse is 0 and false otherwise.
;; Please explictly show your stub, as well as your template.

;; Firework -> Boolean
;; Consumes a Firework, produces true if exploded, false if not
(check-expect (exploded? (make-fw 100 100)) "false")
(check-expect (exploded? (make-fw 100 0)) "true")
(check-expect (exploded? (make-fw 100 -50)) "true")
#;
(define (exploded? fw) ; This is the stub
  (... fw)) 

(define (exploded? fw)
  (cond [(<= (fw-fuse fw) 0) "true"]
        [else "false"]))

;; Given the same Firework data definition, design a function named tick-fw that consumes
;; a Firework and produces a new Firework as follows:
;;   the new firework's y should be the old y minus SPEED.
;;   the new firework's fuse should be 1 less than the old fuse,
;;      except it should never be less than 0
;; the fuse should countdown to 0 (and never go below 0)
;;
;; Please explictly show your stub, as well as your template.
;; Firework -> Firework
;; Consumes a Firework, Produces a firework with originals y minus speed and fuse minus 1
(check-expect (tick-fw (make-fw 100 100)) (make-fw 90 99))
(check-expect (tick-fw (make-fw 50 50)) (make-fw 40 49))
(check-expect (tick-fw (make-fw 0 0)) (make-fw -10 0))
#;
(define (tick-fw fw) ; This is the stub
  (... fw))

(define (tick-fw fw)
  (cond [(> (fw-fuse fw) 0) (make-fw (- (fw-y fw) SPEED)
                                     (- (fw-fuse fw) 1))]
        [(= (fw-fuse fw) 0) (make-fw (- (fw-y fw) 10)
                                     (fw-fuse fw))]))


;; ---------
;; Problem 6
;;
;; Given this partial program:
;;



;; A Bouncing Ball


(require 2htdp/universe)
(require 2htdp/image)

;;
;; Constants:

(define WIDTH 200)
(define HEIGHT 400)

(define X-POS (/ WIDTH 2))

(define BALL (circle 10 "solid" "blue"))

(define MTS (empty-scene WIDTH HEIGHT))

;; Data definitions:

;; Location is Number
;; interp. the y coordinate of a BALL
(define L1 HEIGHT)
(define L2 (/ HEIGHT 2))
(define L3 0)
#;
(define (fn-for-location l)
  (... l))


;; Functions



;; Design a function named render-ball that consumes a Location and 
;; produces an image in which BALL is in the appropriate place on MTS.
;;
;; You may want to use the function place-image, which has the signature
;; Image Number Number Image -> Image. It places its first argument
;; onto its second argument at the x, y position specified by its
;; second and third arguments.

;; Location (number) -> Image
;; consumes a location, produces Ball at location on MTS
(check-expect (render-ball 40) (place-image BALL X-POS 40 MTS))
#;
(define (render-ball loc)
  (... BALL...))

(define (render-ball loc)
  (place-image BALL X-POS loc MTS))






