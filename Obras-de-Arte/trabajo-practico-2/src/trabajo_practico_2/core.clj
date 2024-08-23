(ns trabajo-practico-2.core
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:import [java.lang Math])
  (:import [java.io FileNotFoundException FileReader BufferedReader]))

(java.util.Locale/setDefault java.util.Locale/US)

(def IDX-ARCHIVO-SL 0)
(def IDX-NUM-ITERACIONES 1)
(def IDX-ARCHIVO-SVG 2)
(def CANT-ARG 3)
(def MARGEN 50)
(def DIST 100)

(defn crear-tortuga
  "Devuelve un diccionario que representa una tortuga que dibuja en el archivo SVG."
  [X Y DX DY angulo]
  {:posicion [X Y]
   :angulo angulo
   :direccion [DX DY]})

(defn generar-primera-linea [x-min y-min x-max y-max]
  (format "<svg viewBox=\"%f %f %f %f\" xmlns=\"http://www.w3.org/2000/svg\">\n" x-min y-min x-max y-max))

(defn generar-linea
  "Genera la linea a escribir del archivo con posiciones inicio y fin que se pasan como argumentos"
  [x-inicio y-inicio x-fin y-fin]
  (format "<line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\" stroke-width=\"10\" stroke=\"black\" />\n" x-inicio y-inicio x-fin y-fin))

(defn leer-lineas
  "Devuelve una secuencia con las lineas del archivo"
  [archivo-sl]
  (try 
    (with-open [r (io/reader archivo-sl)]
    (doall (line-seq r)))
    (catch FileNotFoundException e
      (println "Archivo no encontrado:" (.getMessage e))
      nil)
    (catch Exception e
      (println "Error al leer el archivo:" (.getMessage e))
      nil)))

(defn crear-diccionario-reglas
  "Crea un diccionario con las reglas correspondientes a cada caracter que se recibió 
   en el archivo sl"
  [seq-reglas dicc-reglas]
  (if (empty? seq-reglas)
    dicc-reglas
    (let [regla (string/split (first seq-reglas) #"\s+")]
      (crear-diccionario-reglas (drop 1 seq-reglas) (assoc dicc-reglas (first regla) (second regla))))))

(defn actualizar-axioma
  "Realiza la aplicación de las reglas dadas en el archivo (que se encuentran almacenadas 
   en un diccionario) al axioma"
  [axioma nuevo-axioma dicc-reglas]
  (if (empty? axioma)
    nuevo-axioma
    (let [x (str (first axioma))
          _axioma (drop 1 axioma)
          _nuevo-axioma (concat nuevo-axioma (if (contains? dicc-reglas x)
                                               (vec (map str (get dicc-reglas x)))
                                               (vec x)))]
      (actualizar-axioma _axioma _nuevo-axioma dicc-reglas))))

(defn procesar-reglas
  "Dado el axioma inicial aplicamos las reglas recursivamente hasta que se hallan completado 
   la cantidad de iteraciones planteadas"
  [axioma iteraciones dicc-reglas]
  (if (zero? iteraciones)
    axioma
    (procesar-reglas (actualizar-axioma axioma (vec []) dicc-reglas) (dec iteraciones) dicc-reglas)))

(defn _realizar-dibujo
  "Recorre el axioma iterativamente y según lo que lee por cada posición, las tortugas llevan a cabo una acción"
  [axioma angulo]
  (loop [pila (list (crear-tortuga 0 0 1 0 0))
         dibujo (vec [])
         axioma-act axioma]
    (if (empty? axioma-act)
      dibujo
      (let [regla (str (first axioma-act))]
        (cond
          ; Caso en que apilo una tortuga
          (= regla "[")
          (recur (conj pila (peek pila)) dibujo (rest axioma-act))

          ; Caso en que desapilo una tortuga
          (= regla "]")
          (recur (pop pila) dibujo (rest axioma-act))

          ; Caso en que avanza de a un paso. Si la regla es mayuscula se dibuja, sino avanza sin dibujar
          (or (= (clojure.string/upper-case regla) "F") (= (clojure.string/upper-case regla) "G"))
          (let [tortuga-actual (peek pila)
                [x y] (:posicion tortuga-actual)
                [dx dy] (:direccion tortuga-actual)
                tortuga-nueva (assoc tortuga-actual :posicion [(+ x dx) (+ y dy)])
                pila-nueva (conj (pop pila) tortuga-nueva)]
            (recur pila-nueva (if (or (= regla "F") (= regla "G"))
                                (conj dibujo [(float x) (float y) (float (+ x dx)) (float (+ y dy))])
                                dibujo) (rest axioma-act)))

          ; Caso en el que giro
          (or (= regla "+") (= regla "-") (= regla "|"))
          (let [tortuga-actual (peek pila)
                angulo-actual (:angulo tortuga-actual)
                angulo-nuevo ((if (= regla "-")
                                -
                                +) angulo-actual (if (= regla "|")
                                                   Math/PI
                                                   angulo))
                tortuga-nueva (assoc tortuga-actual :angulo angulo-nuevo :direccion [(* DIST (Math/cos angulo-nuevo)) (* DIST (Math/sin angulo-nuevo))])
                pila-nueva (conj (pop pila) tortuga-nueva)]
            (recur pila-nueva dibujo (rest axioma-act)))

          :else
          (recur pila dibujo (rest axioma-act)))))))

(defn hallar-tamanio
  "Itera sobre las medidas del dibujo para encontrar las maximas y minimas"
  [dibujo]
  (let [xs (mapcat (fn [[x1 _ x2 _]] [x1 x2]) dibujo)
        ys (mapcat (fn [[_ y1 _ y2]] [y1 y2]) dibujo)
        x-min (apply min xs)
        y-min (apply min ys)
        x-max (apply max xs)
        y-max (apply max ys)]
    [x-min y-min x-max y-max]))

(defn dibujar-archivo
  "Escribe las líneas del archivo que hacen al dibujo"
  [archivo dibujo]
  (doseq [[x-inicio y-inicio x-fin y-fin] dibujo]
    (spit archivo (generar-linea x-inicio y-inicio x-fin y-fin) :append true)))

(defn escribir-archivo
  "Escribe las principales lineas del archivo"
  [archivo dibujo]
  (let [[x-min y-min x-max y-max] (hallar-tamanio dibujo)
        x (- x-min MARGEN)
        y (- y-min MARGEN)
        ancho (+ (* 2 MARGEN) (- x-max x-min))
        alto (+ (* 2 MARGEN) (- y-max y-min))]
    (spit archivo (generar-primera-linea x y ancho alto))
    (dibujar-archivo archivo dibujo)
    (spit archivo "</svg>\n" :append true)))

(defn realizar-dibujo
  "Realiza la llamada inicial a la funcion recursiva que se encarga de ver cada elemento del axioma
   y escribir o hacer lo correspondiente en el archivo svg"
  [archivo-svg axioma angulo]
  (let [dibujo (_realizar-dibujo axioma angulo)]
    (escribir-archivo archivo-svg dibujo)))

(defn procesar-informacion
  "Recibe los datos leídos en la línea de comandos y estructura los datos de la manera necesaria
   para ser aplicados en las funciones a utilizar a lo largo del programa"
  [archivo-sl archivo-svg iteraciones]
   (if (not (neg? iteraciones))
    (let [info (leer-lineas archivo-sl)]  
      (when info  
        (let [angulo (Math/toRadians (Float/parseFloat (first info)))
              axioma (vec (seq (second info)))
              dicc-reglas (crear-diccionario-reglas (drop 2 info) (hash-map))
              axioma-final (procesar-reglas axioma iteraciones dicc-reglas)]
          (realizar-dibujo archivo-svg axioma-final angulo)))) 
              (do ; Manejo de la condición cuando iteraciones es inválido o negativo
                  (spit archivo-svg (generar-primera-linea (double 0) (double 0) (double 100) (double 100)))
                  (spit archivo-svg "</svg>\n" :append true))))


(defn -main [& args]
  (if (or (empty? args) (< (count args) 3))  ; Asegura que hay al menos 3 argumentos
    (do
      (println "Uso: lein run <archivo-sl> <numero-de-iteraciones> <archivo-svg>")
      '())
    (let [archivo-sl (nth args 0)
          iteraciones  (nth args 1)
          archivo-svg (nth args 2)]
          (procesar-informacion archivo-sl archivo-svg (Integer/parseInt iteraciones)))))