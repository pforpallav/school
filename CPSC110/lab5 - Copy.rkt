;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname lab5) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
;; Space Invaders

(require 2htdp/universe)
(require 2htdp/image)

;; Constants:

(define HEIGHT 400)
(define WIDTH  200)

(define UFO-X-SPEED 1.5)
(define UFO-Y-SPEED 1.5)
(define TANK-SPEED 2)
(define MISSILE-SPEED 10)

(define HIT-RANGE 10)

(define BACKGROUND (empty-scene WIDTH HEIGHT))

(define UFO
  (overlay/xy (ellipse 10 15 "outline" "blue")   ; cockpit cover
              -5 6
              (ellipse 20 10 "solid"   "blue"))) ; saucer

(define TANK
  (overlay/xy
   (overlay (ellipse 28 8 "solid" "black")    ; tread center
            (ellipse 30 10 "solid" "green"))  ; tread outline
   5 -14
   (above (rectangle 5 10 "solid" "black")    ; gun
          (rectangle 20 10 "solid" "black")))); main body

(define MISSILE (ellipse 5 15 "solid" "red"))



;; Data Definitions:


(define-struct tank (loc vel))
;; Tank is (make-tank Number Number)
;; interp. (make-tank x dx) means the tank is at (x, HEIGHT)
;;   and that it moves dx pixels per clock tick
(define tank1 (make-tank (/ WIDTH 2) 0)) ;middle
(define tank2 (make-tank 0 0)) ;most left
(define tank3 (make-tank WIDTH 0)) ;most right
#;
(define (fn-for-tank tank)
  (... (tank-loc tank)
       (tank-vel tank)))

(define-struct ufo (x y dx))
;; UFO is (make-ufo Number Number Number)
;; interp. (make-ufo x y dx) means the ufo is at (x, y)
;;   and that it moves along x by dx pixels per clock tick
(define ufo1 (make-ufo 0 (/ HEIGHT 3) 10)) ;most left, 1/3 HEIGHT, vel 10
(define ufo2 (make-ufo (/ WIDTH 2) (/ HEIGHT 3) 10)) ;center, 1/3 HEIGHT, vel 10
(define ufo3 (make-ufo WIDTH (/ HEIGHT 3) 10)) ;most right, 1/3 HEIGHT, vel 10
#;
(define (fn-for-ufo ufo)
  (... (ufo-x ufo)
       (ufo-y ufo)
       (ufo-dx ufo)))

(define-struct missile (x y))
;; Missile is (make-missile Number Number)
;; interp. (make-missile x y) is the missile's current x, y location
(define missile1 (make-missile (/ WIDTH 2) (/ HEIGHT 2))) ;center, 1/2 HEIGHT

#;
(define (fn-for-missile mis)
  (... (missile-x mis)
       (missile-y mis)))

(define-struct aim   (ufo tank))
(define-struct fired (ufo tank missile))
;; SIGS (space invader game state) is one of:
;; – (make-aim UFO Tank)
;; – (make-fired UFO Tank Missile)
;; interp. the state of the game, either aiming or a missile has been fired
(define SIGS1 (make-aim ufo1 tank1))
(define SIGS2 (make-fired ufo1 tank1 missile1))
#;
(define (fn-for-SIGS sigs)
  (cond [(aim? sigs) (... (fn-for-ufo (aim-ufo sigs))
                          (fn-for-tank (aim-tank sigs)))]
        [(fired? sigs) (... (fn-for-ufo (fired-ufo sigs))
                            (fn-for-tank (fired-tank sigs))
                            (fn-for-missile (fired-missile sigs)))]))


;; Functions:


;; runs the program
(define (main ufo-height)
  ;; SIGS -> SIGS
  (big-bang (make-aim (make-ufo (/ WIDTH 2) ufo-height UFO-X-SPEED)
                      (make-tank (/ WIDTH 2) 0))
            (to-draw si-render)
            (on-key  si-control)
            (on-tick si-move)
            (stop-when si-game-over?)))


;; SIGS -> Image
;; render ufo, tank and possibly missile onto BACKGROUND
;; <COPIED DIRECTLY FROM BOOK>
(define (si-render s)
  (cond
    [(aim? s)
     (tank-render (aim-tank s)
                  (ufo-render (aim-ufo s) BACKGROUND))]
    [(fired? s)
     (tank-render (fired-tank s)
                  (ufo-render (fired-ufo s)
                              (missile-render (fired-missile s)
                                              BACKGROUND)))]))

;; UFO Image -> Image
;; add UFO to the given image as specified by u
(check-expect (ufo-render (make-ufo 20 25 UFO-X-SPEED) (empty-scene 40 50))
              (place-image/align UFO 20 25 "center" "bottom" (empty-scene 40 50)))

(define (ufo-render u im)
  (place-image/align UFO (ufo-x u) (ufo-y u) "center" "bottom" im))

;; Tank Image -> Image
;; add TANK to the given image as specified by t
(check-expect (tank-render (make-tank (/ WIDTH 2) 1) BACKGROUND)
              (place-image/align TANK
                                 (/ WIDTH 2)
                                 HEIGHT
                                 "center" "bottom"
                                 BACKGROUND))

(define (tank-render t im)
  (place-image/align TANK (tank-loc t) HEIGHT "center" "bottom" im))

;; Missile Image -> Image
;; add m to the given scene im
(check-expect (missile-render (make-missile 25 36) (empty-scene 60 60))
              (place-image/align MISSILE 25 36 "center" "bottom" (empty-scene 60 60)))

(define (missile-render m im) 
  (place-image/align MISSILE (missile-x m) (missile-y m) "center" "bottom" im))

;; SIGS KeyEvent -> SIGS
;; if in aim state, space fires missile, left/right change tank direction
;; if in fired state, left/right change tank direction
(check-expect
 (si-control (make-aim (make-ufo 200 300 1.5) (make-tank 100 0)) " ")
 (make-fired (make-ufo 200 300 1.5) (make-tank 100 0) (make-missile 100 (- HEIGHT (image-height TANK)))))
(check-expect
 (si-control (make-aim (make-ufo 200 300 1.5) (make-tank 100 0)) "left")
 (make-aim   (make-ufo 200 300 1.5) (make-tank 100 (- TANK-SPEED))))
(check-expect
 (si-control (make-aim (make-ufo 200 300 1.5) (make-tank 100 0)) "right")
 (make-aim   (make-ufo 200 300 1.5) (make-tank 100 TANK-SPEED)))

(check-expect (si-control (make-fired (make-ufo 200 300 1.5)
                                      (make-tank 100 0) 
                                      (make-missile 100 150))
                          " ")
              (make-fired (make-ufo 200 300 1.5) 
                          (make-tank 100 0)
                          (make-missile 100 150)))
(check-expect (si-control (make-fired (make-ufo 200 300 1.5)
                                      (make-tank 100 0)
                                      (make-missile 100 150))
                          "left")
              (make-fired (make-ufo 200 300 1.5)
                          (make-tank 100 (- TANK-SPEED))
                          (make-missile 100 150)))
(check-expect (si-control (make-fired (make-ufo 200 300 1.5)
                                      (make-tank 100 0)
                                      (make-missile 100 150))
                          "right")
              (make-fired  (make-ufo 200 300 1.5)
                           (make-tank 100 TANK-SPEED)
                           (make-missile 100 150)))

(define (si-control s key)
  (cond
    [(aim? s)
     (cond [(key=? key " ")
            (make-fired (aim-ufo s)
                        (aim-tank s)
                        (tank-fire (aim-tank s)))]
           [else
            (make-aim (aim-ufo s)
                      (tank-control (aim-tank s) key))])]
    [(fired? s)
     (cond [(key=? key " ") s]
           [else
            (make-fired (fired-ufo s)
                        (tank-control (fired-tank s) key)
                        (fired-missile s))])]))

;; Tank -> Missile
;; produces a missile that is fired from the given tank
(check-expect (tank-fire (make-tank 10 TANK-SPEED))
              (make-missile 10 (- HEIGHT (image-height TANK))))

(define (tank-fire t)
  (make-missile (tank-loc t) (- HEIGHT (image-height TANK))))

;; Tank KeyEvent -> Tank
;; changes the direction of the tank according to KeyEvent
(check-expect (tank-control (make-tank 10 TANK-SPEED) "right")
              (make-tank 10 TANK-SPEED))
(check-expect (tank-control (make-tank 10 (- TANK-SPEED)) "right")
              (make-tank 10 TANK-SPEED))
(check-expect (tank-control (make-tank 10 TANK-SPEED) "left")
              (make-tank 10 (- TANK-SPEED)))
(check-expect (tank-control (make-tank 10 (- TANK-SPEED)) "left")
              (make-tank 10 (- TANK-SPEED)))
(check-expect (tank-control (make-tank 10 TANK-SPEED) " ")
              (make-tank 10 TANK-SPEED))

(define (tank-control t key)
  (cond [(key=? key "left")
         (make-tank (tank-loc t) (- TANK-SPEED))]
        [(key=? key "right")
         (make-tank (tank-loc t) TANK-SPEED)]
        [else t]))



;;----------------------------------------------------------------------------


;; SIGS -> SIGS
;; moves tank, ufo and possibly missile
#;
(define (si-move s)
  s)
#;
(define (si-move s)
  (cond [(aim? s) (make-aim (make-ufo (cond [(< (ufo-x (aim-ufo s)) 0)
                                             (+ (ufo-x (aim-ufo s)) UFO-X-SPEED)]
                                            [(> (ufo-x (aim-ufo s)) WIDTH)
                                             (- (ufo-x (aim-ufo s)) UFO-X-SPEED)]
                                            [else (+ (ufo-x (aim-ufo s)) UFO-X-SPEED)])
                                      (ufo-y (aim-ufo s))
                                      (ufo-dx (aim-ufo s)))
                            (make-tank (+ (tank-loc (aim-tank s)) 
                                          (tank-vel (aim-tank s)))
                                       (tank-vel (aim-tank s))))]
        [(fired? s) (make-fired (make-ufo (cond [(= (ufo-x (fired-ufo s)) 0)
                                                 (+ (ufo-x (fired-ufo s)) UFO-X-SPEED)]
                                                [(= (ufo-x (fired-ufo s)) WIDTH)
                                                 (- (ufo-x (fired-ufo s)) UFO-X-SPEED)]
                                                [else (+ (ufo-x (fired-ufo s)) UFO-X-SPEED)])
                                          (ufo-y (fired-ufo s))
                                          (ufo-dx (fired-ufo s)))
                                (make-tank (+ (tank-loc (fired-tank s)) 
                                              (tank-vel (fired-tank s)))
                                           (tank-vel (fired-tank s)))
                                (make-missile (tank-loc (fired-tank s)) MISSILE-SPEED))]))


(define (si-move s)
  (cond [(aim? s) (make-aim (make-ufo (cond [(< (ufo-x (aim-ufo s)) 0)
                                             (make-aim (make-ufo (+ (ufo-x (aim-ufo s)) UFO-X-SPEED)
                                                                 (ufo-y (aim-ufo s))
                                                                 (ufo-dx (aim-ufo s)))
                                                       (make-tank (+ (tank-loc (aim-tank s)) 
                                                                     (tank-vel (aim-tank s)))
                                                                  (tank-vel (aim-tank s))))]
                                            [(> (ufo-x (aim-ufo s)) WIDTH)
                                             (make-aim (make-ufo (- (ufo-x (aim-ufo s)) UFO-X-SPEED)
                                                                 (ufo-y (aim-ufo s))
                                                                 (ufo-dx (aim-ufo s)))
                                                       (make-tank (+ (tank-loc (aim-tank s)) 
                                                                     (tank-vel (aim-tank s)))
                                                                  (tank-vel (aim-tank s))))]
                                            [else (+ (ufo-x (aim-ufo s)) UFO-X-SPEED)])
                                      (ufo-y (aim-ufo s))
                                      (ufo-dx (aim-ufo s)))
                            (make-tank (+ (tank-loc (aim-tank s)) 
                                          (tank-vel (aim-tank s)))
                                       (tank-vel (aim-tank s))))]
        [(fired? s) (make-fired (make-ufo (cond [(= (ufo-x (fired-ufo s)) 0)
                                                 (+ (ufo-x (fired-ufo s)) UFO-X-SPEED)]
                                                [(= (ufo-x (fired-ufo s)) WIDTH)
                                                 (- (ufo-x (fired-ufo s)) UFO-X-SPEED)]
                                                [else (+ (ufo-x (fired-ufo s)) UFO-X-SPEED)])
                                          (ufo-y (fired-ufo s))
                                          (ufo-dx (fired-ufo s)))
                                (make-tank (+ (tank-loc (fired-tank s)) 
                                              (tank-vel (fired-tank s)))
                                           (tank-vel (fired-tank s)))
                                (make-missile (tank-loc (fired-tank s)) MISSILE-SPEED))]))

;; SIGS -> Boolean
;; returns true if ufo lands or missile hits ufo
(define (si-game-over? s)
  false)

(main 99)
