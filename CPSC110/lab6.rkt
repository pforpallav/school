;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname lab6) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
;; Lab 6 - Specific XML structures and query requests

(require (planet "web-services.rkt" ("ubccs110" "web-services.plt" 1 0)))


;; Constants:

(define CAD/USD 1.0365)


;; Data definitions:


;(define-struct ebay-item (title price pcode location))
;; EbayItem is (make-ebay-item String String String String)
;; interp. the title, price, postal code, and state and country
;; information of an item listed on eBay
(define eitem1 (make-ebay-item "test1" "1.00" "V1V1V1" "British Columbia, Canada"))
(define eitem2 (make-ebay-item "test2" "2.00" "V2V2V2" "British Columbia, Canada"))
#;
(define (fn-for-ebay-item ei)
  (... (fn-for-ebayitem (ebay-item-title ei))
       (ebay-item-price ei)
       (ebay-item-pcode ei)
       (ebay-item-location)))


;; ListOfEbayItem is one of:
;; - empty
;; - (cons EbayItem ListOfEbayItem)
;; interp. a list of items returned from eBay
(define LOEI1 empty)
(define LOEI2 (cons eitem1 
                    (cons eitem2 empty)))
#;
(define (fn-for-loei loei)
  (cond [(empty? loei) (...)]
        [else
         (... (fn-for-ebay-item (first loei))
              (fn-for-loei (rest loei)))]))


;; NameValuePair is (cons String (cons String empty))
;; interp. a single URL query argument
(define NVP1 (cons "markers" (cons "V1V1V1" empty)))
(define NVP2 (cons "markers" (cons "V2V2V2" empty)))
#;
(define (fn-for-nvp nvp)
  (... (first nvp)
       (first (rest nvp))))


;; ListOfNameValuePair is one of:
;; - empty
;; - (cons NameValuePair ListOfNameValuePair)
;; interp. multiple URL query arguments
(define LONVP1 empty)
(define LONVP2 (cons NVP1 (cons NVP2 empty)))
(define LONVP3 (cons (cons "markers" (cons "V8V3X4" empty))
                     (cons (cons "markers" (cons "V6T1Z4" empty))
                           empty)))
#;
(define (fn-for-lonvp lonvp)
  (cond [(empty? lonvp) (...)]
        [else
         (... (fn-for-nvp (first lonvp))
              (fn-for-lonvp (rest lonvp)))]))

(send-request-png
 (make-query-url "maps.google.com"
                 "maps/api/staticmap"
                 (cons (cons "size" (cons "256x256" empty)) 
                       (cons (cons "sensor" (cons "false" empty))
                             (cons (cons "markers" (cons "V6T1Z4" empty))
                                   (cons NVP2
                                         empty))))))

;; URL is String
;; interp. Uniform Resource Locator specifying the address of a web page

(define URL "http://maps.google.com/maps/api/staticmap?size=256x256&sensor=false&markers=V4B3T6&markers=V6G2A1&markers=V7S2W6")

#;
(define (fn-for-url url)
  (... url))


;; Host is String
;; interp. a main web site address

(define GOOGLE-HOST "maps.google.com")
(define EBAY-HOST   "svcs.ebay.com")

#;
(define (fn-for-host host)
  (... host))


;; Path is String
;; interp. the path to a resource or program on a web site

(define GOOGLE-PATH "maps/api/staticmap")
(define EBAY-PATH   "services/search/FindingService/v1")

#;
(define (fn-for-path path)
  (... path))



;; Functions:


;; ListOfEbayItem -> ListOfEbayItem
;; consumes a loei with prices in USD and converts to loei with prices in CAD
#;
(define (usd->cad-loei loei) ;stub
  empty)
(check-expect (usd->cad empty) empty)
(check-expect (usd->cad LOEI2)
              (cons (make-ebay-item (ebay-item-title (first LOEI2))
                                    (number->decimal-string (* CAD/USD
                                                       (string->number (ebay-item-price (first LOEI2)))))
                                    (ebay-item-pcode (first LOEI2))
                                    (ebay-item-location (first LOEI2)))
                    (cons (make-ebay-item (ebay-item-title (first (rest LOEI2)))
                                          (number->decimal-string (* CAD/USD
                                                             (string->number (ebay-item-price (first (rest LOEI2))))))
                                          (ebay-item-pcode (first (rest LOEI2)))
                                          (ebay-item-location (first (rest LOEI2))))
                          empty)))

(define (usd->cad loei)
  (cond [(empty? loei) empty]
        [else
         (cons (ebay-cad (first loei))
               (usd->cad (rest loei)))]))

;; Ebay-Item-> Ebay-Item
;; Consumes an Ebay-item, produces an Ebay-Item with modified $
(check-expect (ebay-cad (make-ebay-item "test" "1" "V1V1V1" "British Columbia, Canada"))
                        (make-ebay-item "test" "1.04" "V1V1V1" "British Columbia, Canada"))
(define (ebay-cad ei)
  (make-ebay-item (ebay-item-title ei)
                               (number->decimal-string (* CAD/USD 
                                                  (string->number (ebay-item-price ei))))
                               (ebay-item-pcode ei)
                               (ebay-item-location ei)))

;; ListOfEbayItem -> ListOfNameValuePair
;; Consumes a ListOfEbayItem, Produces a ListOfNameValuePair
#;
(define (convert-to-marker-pairs loei)
  (empty))

(check-expect (convert-to-marker-pairs empty) empty)
(check-expect (convert-to-marker-pairs LOEI2)
              (cons (cons "markers" (cons (ebay-item-pcode (first LOEI2)) empty))
                    (cons (cons "markers" (cons (ebay-item-pcode (first (rest LOEI2))) empty))
                          empty)))

(define (convert-to-marker-pairs loei)
  (cond [(empty? loei) empty]
        [else
         (cons (marker-pairs (first loei))
               (convert-to-marker-pairs (rest loei)))]))


;; Ebay-Item-> NameValuePair
;; Cosumes an ebay-item, produces a NameValuePair
(check-expect (marker-pairs (make-ebay-item "test" "1.00" "V1V1V1" "British Columbia, Canada"))
              (cons "markers" (cons "V1V1V1" empty)))
(define (marker-pairs ei)
  (cons "markers" (cons (ebay-item-pcode ei) empty)))

;; ListOfNameValuePair -> Image
;; Consumes a ListofNameValuePair, renders the image with markers
#;
(define (get-map lonvp)
  (image))

(define (get-map lonvp)
  (send-request-png
   (make-query-url "maps.google.com"
                   "maps/api/staticmap"
                   (cons (cons "size" (cons "256x256" empty)) 
                         (cons (cons "sensor" (cons "false" empty))
                               lonvp)))))

;; String -> Image
;; Consumes a search (string), produces an map (image)
#;
(define (get-ebi-map "title")
  (map))

(define (get-ebi-map title)
  (get-map 
   (convert-to-marker-pairs 
    (get-ebay-items title))))
