-- =====================================================
-- SGS · Seed extendido con imágenes
-- Idempotente: usa INSERT IGNORE / UPDATE WHERE
-- Imágenes via loremflickr (CC de Flickr) con lock estable
-- =====================================================

-- ----- 1. Categorías nuevas (las 10 originales ya existen) -----
INSERT IGNORE INTO categorias (nombre) VALUES
  ('Congelados'),
  ('Dulces'),
  ('Mascotas'),
  ('Bebes');

-- ----- 2. Imágenes para productos EXISTENTES -----
-- Lacteos
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/yogurt?lock=11' WHERE nombre = 'Yogurt Natural';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/cheese,white?lock=12' WHERE nombre = 'Queso Campesino';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/cream,milk?lock=13' WHERE nombre = 'Crema de Leche';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/yogurt,drink?lock=14' WHERE nombre = 'Kumis';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/cheese,mozzarella?lock=15' WHERE nombre = 'Queso Doble Crema';

-- Granos
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/sugar?lock=21' WHERE nombre = 'Azucar';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/oats,oatmeal?lock=22' WHERE nombre = 'Avena';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/spaghetti,pasta?lock=23' WHERE nombre = 'Pasta Espagueti';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/flour,wheat?lock=24' WHERE nombre = 'Harina de Trigo';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/popcorn,corn?lock=25' WHERE nombre = 'Maiz Pira';

-- Frutas
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/banana?lock=31' WHERE nombre = 'Banano';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/apple,red?lock=32' WHERE nombre = 'Manzana Roja';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/orange,fruit?lock=33' WHERE nombre = 'Naranja';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/lime,lemon?lock=34' WHERE nombre = 'Limon';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/pineapple?lock=35' WHERE nombre = 'Pina';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/strawberry?lock=36' WHERE nombre = 'Fresa';

-- Panaderia
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/sliced,bread?lock=41' WHERE nombre = 'Pan Tajado';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/bun,bread?lock=42' WHERE nombre = 'Mogolla';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/whole,wheat,bread?lock=43' WHERE nombre = 'Pan Integral';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/crackers,saltine?lock=44' WHERE nombre = 'Galletas de Soda';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/donut,cheese,ball?lock=45' WHERE nombre = 'Bunuelo';

-- Bebidas
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/water,bottle?lock=51' WHERE nombre = 'Agua';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/cola,soda?lock=52' WHERE nombre = 'Gaseosa';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/orange,juice?lock=53' WHERE nombre = 'Jugo de Naranja';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/coffee,beans?lock=54' WHERE nombre = 'Cafe';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/hot,chocolate?lock=55' WHERE nombre = 'Chocolate';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/beer,bottle?lock=56' WHERE nombre = 'Cerveza';

-- Carnes
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/chicken,breast,raw?lock=61' WHERE nombre = 'Pechuga de Pollo';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/ground,beef?lock=62' WHERE nombre = 'Carne Molida';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/sausage,chorizo?lock=63' WHERE nombre = 'Chorizo';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/eggs,carton?lock=64' WHERE nombre = 'Huevos';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/hotdog,sausage?lock=65' WHERE nombre = 'Salchicha';

-- Verduras
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/tomato?lock=71' WHERE nombre = 'Tomate';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/onion?lock=72' WHERE nombre = 'Cebolla Cabezona';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/potato?lock=73' WHERE nombre = 'Papa';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/carrot?lock=74' WHERE nombre = 'Zanahoria';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/avocado?lock=75' WHERE nombre = 'Aguacate';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/plantain,green?lock=76' WHERE nombre = 'Platano';

-- Snacks
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/potato,chips?lock=81' WHERE nombre = 'Papas Fritas';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/chocolate,cake,bar?lock=82' WHERE nombre = 'Chocorramo';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/oreo,cookies?lock=83' WHERE nombre = 'Galletas Oreo';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/peanuts?lock=84' WHERE nombre = 'Mani Salado';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/corn,chips?lock=85' WHERE nombre = 'Choclitos';

-- Aseo
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/soap,bar?lock=91' WHERE nombre = 'Jabon de Bano';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/shampoo,bottle?lock=92' WHERE nombre = 'Shampoo';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/toilet,paper?lock=93' WHERE nombre = 'Papel Higienico';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/laundry,detergent?lock=94' WHERE nombre = 'Detergente';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/toothpaste?lock=95' WHERE nombre = 'Crema Dental';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/dish,soap?lock=96' WHERE nombre = 'Jabon Lavavajillas';

-- Enlatados
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/canned,tuna?lock=101' WHERE nombre = 'Atun';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/sardines,can?lock=102' WHERE nombre = 'Sardinas';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/ketchup,bottle?lock=103' WHERE nombre = 'Salsa de Tomate';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/canned,corn?lock=104' WHERE nombre = 'Maiz Tierno';
UPDATE productos SET imagen_url = 'https://loremflickr.com/400/400/canned,sausages?lock=105' WHERE nombre = 'Salchicha Enlatada';

-- ----- 3. Productos NUEVOS por categoría -----
-- (Usamos INSERT IGNORE con UNIQUE virtual via nombre — pero como nombre no es único,
--  usamos NOT EXISTS para evitar duplicados al re-ejecutar)

-- Lacteos (categoria_id=1)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Leche Entera' AS n, 'Leche entera pasteurizada de vaca, fuente esencial de calcio, proteinas y vitamina D. Ideal para el desayuno, café y postres.\n\nLeche Alqueria 1100 ml' AS d, 4800.00 AS p, 90 AS s, 1 AS a, 1 AS c, 'https://loremflickr.com/400/400/milk,bottle?lock=110' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Leche Entera');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Mantequilla' AS n, 'Mantequilla cremosa de leche de vaca, ideal para untar pan, cocinar y reposteria.\n\nMantequilla Alpina 250 gr' AS d, 7200.00 AS p, 50 AS s, 1 AS a, 1 AS c, 'https://loremflickr.com/400/400/butter?lock=111' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Mantequilla');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Yogurt Griego' AS n, 'Yogurt griego cremoso, alto en proteinas y bajo en azúcar. Excelente para desayunos saludables.\n\nYogurt Griego Alpina 150 gr' AS d, 4200.00 AS p, 60 AS s, 1 AS a, 1 AS c, 'https://loremflickr.com/400/400/greek,yogurt?lock=112' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Yogurt Griego');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Arequipe' AS n, 'Dulce tradicional colombiano elaborado con leche y azúcar cocinados lentamente. Perfecto para postres y obleas.\n\nArequipe Alpina 250 gr' AS d, 6500.00 AS p, 55 AS s, 1 AS a, 1 AS c, 'https://loremflickr.com/400/400/dulce,leche,caramel?lock=113' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Arequipe');

-- Granos (categoria_id=2)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Arroz Blanco' AS n, 'Arroz blanco de grano largo, base de la alimentacion colombiana. Versátil y de cocción uniforme.\n\nArroz Diana 500 gr' AS d, 3200.00 AS p, 120 AS s, 1 AS a, 2 AS c, 'https://loremflickr.com/400/400/rice,white?lock=210' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Arroz Blanco');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Frijol Rojo' AS n, 'Frijol rojo seco, alto en proteinas y fibra. Base del frijolada paisa y la alimentacion colombiana.\n\nFrijol Rojo Diana 500 gr' AS d, 6800.00 AS p, 70 AS s, 1 AS a, 2 AS c, 'https://loremflickr.com/400/400/red,beans?lock=211' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Frijol Rojo');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Lentejas' AS n, 'Lentejas secas pardinas, fuente de proteínas vegetales, hierro y fibra. Ideales para sopas y guisos.\n\nLentejas Diana 500 gr' AS d, 5400.00 AS p, 60 AS s, 1 AS a, 2 AS c, 'https://loremflickr.com/400/400/lentils?lock=212' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Lentejas');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Garbanzos' AS n, 'Garbanzos secos, ricos en proteínas y fibra. Versátiles para sopas, guisos, hummus y ensaladas.\n\nGarbanzos Doria 500 gr' AS d, 6200.00 AS p, 50 AS s, 1 AS a, 2 AS c, 'https://loremflickr.com/400/400/chickpeas?lock=213' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Garbanzos');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Sal' AS n, 'Sal refinada para cocinar y sazonar. Ingrediente fundamental en cualquier cocina.\n\nSal Refisal 1000 gr' AS d, 2800.00 AS p, 100 AS s, 1 AS a, 2 AS c, 'https://loremflickr.com/400/400/salt?lock=214' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Sal');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Aceite Vegetal' AS n, 'Aceite vegetal de girasol y soya para cocinar y freír. Ideal para uso diario en la cocina.\n\nAceite Premier 1000 ml' AS d, 11500.00 AS p, 65 AS s, 1 AS a, 2 AS c, 'https://loremflickr.com/400/400/vegetable,oil?lock=215' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Aceite Vegetal');

-- Frutas (categoria_id=3)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Mango' AS n, 'Mango Tommy maduro, fruta tropical jugosa y dulce. Rico en vitamina C, A y antioxidantes.\n\nMango Tommy por unidad' AS d, 2800.00 AS p, 60 AS s, 1 AS a, 3 AS c, 'https://loremflickr.com/400/400/mango?lock=310' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Mango');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Uvas' AS n, 'Uvas verdes sin semilla, dulces y jugosas. Perfectas como snack o para postres.\n\nUvas verdes bandeja 500 gr' AS d, 8500.00 AS p, 35 AS s, 1 AS a, 3 AS c, 'https://loremflickr.com/400/400/grapes?lock=311' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Uvas');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Sandia' AS n, 'Sandía fresca, fruta refrescante de pulpa roja y alto contenido de agua. Perfecta para días calurosos.\n\nSandía por unidad' AS d, 9500.00 AS p, 25 AS s, 1 AS a, 3 AS c, 'https://loremflickr.com/400/400/watermelon?lock=312' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Sandia');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Papaya' AS n, 'Papaya madura, fruta tropical de pulpa naranja, dulce y digestiva. Rica en vitamina C y enzimas.\n\nPapaya por libra' AS d, 3200.00 AS p, 45 AS s, 1 AS a, 3 AS c, 'https://loremflickr.com/400/400/papaya?lock=313' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Papaya');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Mora' AS n, 'Mora colombiana fresca, ácida y aromática. Ideal para jugos, postres y mermeladas.\n\nMora bandeja 500 gr' AS d, 5500.00 AS p, 40 AS s, 1 AS a, 3 AS c, 'https://loremflickr.com/400/400/blackberry?lock=314' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Mora');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Maracuya' AS n, 'Maracuyá fresco, fruta tropical de pulpa amarilla aromática y ácida. Perfecto para jugos refrescantes.\n\nMaracuyá por libra' AS d, 4200.00 AS p, 50 AS s, 1 AS a, 3 AS c, 'https://loremflickr.com/400/400/passion,fruit?lock=315' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Maracuya');

-- Bebidas (categoria_id=5)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Te Verde' AS n, 'Té verde en bolsitas, antioxidante natural y energizante saludable.\n\nTé Hindu Verde caja x25' AS d, 5800.00 AS p, 50 AS s, 1 AS a, 5 AS c, 'https://loremflickr.com/400/400/green,tea?lock=510' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Te Verde');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Bebida Energetica' AS n, 'Bebida energizante con cafeína, taurina y vitaminas. Ideal para mantener la energía durante el día.\n\nRed Bull 250 ml' AS d, 6500.00 AS p, 45 AS s, 1 AS a, 5 AS c, 'https://loremflickr.com/400/400/energy,drink?lock=511' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Bebida Energetica');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Vino Tinto' AS n, 'Vino tinto Cabernet Sauvignon. Acompañamiento perfecto para reuniones y comidas especiales.\n\nVino Tinto Casillero del Diablo 750 ml' AS d, 32500.00 AS p, 25 AS s, 1 AS a, 5 AS c, 'https://loremflickr.com/400/400/red,wine?lock=512' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Vino Tinto');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Aromatica' AS n, 'Aromática de manzanilla, ideal para relajarse antes de dormir y aliviar molestias digestivas.\n\nManzanilla Hindu caja x25' AS d, 4500.00 AS p, 60 AS s, 1 AS a, 5 AS c, 'https://loremflickr.com/400/400/chamomile,tea?lock=513' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Aromatica');

-- Carnes (categoria_id=6)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Lomo de Res' AS n, 'Lomo de res suave, corte premium ideal para asar a la parrilla o sellar a la plancha.\n\nLomo de res por libra' AS d, 22500.00 AS p, 25 AS s, 1 AS a, 6 AS c, 'https://loremflickr.com/400/400/beef,steak?lock=610' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Lomo de Res');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Costilla de Cerdo' AS n, 'Costilla de cerdo fresca, ideal para BBQ, parrilla y sancochos. Sabor jugoso y tradicional.\n\nCostilla de cerdo por libra' AS d, 13800.00 AS p, 30 AS s, 1 AS a, 6 AS c, 'https://loremflickr.com/400/400/pork,ribs?lock=611' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Costilla de Cerdo');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Tilapia' AS n, 'Tilapia fresca en filetes, pescado blanco de sabor suave y bajo en grasa. Ideal a la plancha.\n\nFilete de Tilapia por libra' AS d, 12500.00 AS p, 25 AS s, 1 AS a, 6 AS c, 'https://loremflickr.com/400/400/tilapia,fish?lock=612' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Tilapia');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Jamon' AS n, 'Jamón de cerdo cocido y rebanado, ideal para sandwiches, desayunos y picadas.\n\nJamón Zenu paquete 250 gr' AS d, 7800.00 AS p, 50 AS s, 1 AS a, 6 AS c, 'https://loremflickr.com/400/400/sliced,ham?lock=613' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Jamon');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Tocineta' AS n, 'Tocineta ahumada, perfecta para desayunos, hamburguesas y recetas con sabor ahumado.\n\nTocineta Zenu 200 gr' AS d, 9200.00 AS p, 35 AS s, 1 AS a, 6 AS c, 'https://loremflickr.com/400/400/bacon?lock=614' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Tocineta');

-- Verduras (categoria_id=7)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Lechuga' AS n, 'Lechuga crespa fresca, base de ensaladas y sandwiches. Crujiente y saludable.\n\nLechuga crespa por unidad' AS d, 2200.00 AS p, 60 AS s, 1 AS a, 7 AS c, 'https://loremflickr.com/400/400/lettuce?lock=710' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Lechuga');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Pepino Cohombro' AS n, 'Pepino cohombro fresco, refrescante y ligero. Ideal para ensaladas, jugos y aguas saborizadas.\n\nPepino cohombro por libra' AS d, 2500.00 AS p, 70 AS s, 1 AS a, 7 AS c, 'https://loremflickr.com/400/400/cucumber?lock=711' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Pepino Cohombro');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Pimenton' AS n, 'Pimentón rojo fresco, dulce y crujiente. Ideal para guisos, hogao, ensaladas y salsas.\n\nPimentón rojo por libra' AS d, 3800.00 AS p, 55 AS s, 1 AS a, 7 AS c, 'https://loremflickr.com/400/400/red,bell,pepper?lock=712' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Pimenton');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Brocoli' AS n, 'Brócoli fresco, vegetal nutritivo lleno de vitaminas y antioxidantes. Ideal al vapor o salteado.\n\nBrócoli por unidad' AS d, 4500.00 AS p, 40 AS s, 1 AS a, 7 AS c, 'https://loremflickr.com/400/400/broccoli?lock=713' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Brocoli');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Espinaca' AS n, 'Espinaca fresca en hojas, rica en hierro, ácido fólico y vitaminas. Versátil en ensaladas y guisos.\n\nEspinaca paquete 250 gr' AS d, 3500.00 AS p, 45 AS s, 1 AS a, 7 AS c, 'https://loremflickr.com/400/400/spinach?lock=714' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Espinaca');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Yuca' AS n, 'Yuca fresca, tubérculo tradicional colombiano. Indispensable en sancochos y patacones.\n\nYuca por libra' AS d, 2200.00 AS p, 80 AS s, 1 AS a, 7 AS c, 'https://loremflickr.com/400/400/cassava,yuca?lock=715' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Yuca');

-- Snacks (categoria_id=8)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Doritos' AS n, 'Doritos sabor queso, snack crujiente y sabroso. Perfecto para reuniones y antojos.\n\nDoritos Mexicana 130 gr' AS d, 5800.00 AS p, 65 AS s, 1 AS a, 8 AS c, 'https://loremflickr.com/400/400/tortilla,chips?lock=810' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Doritos');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Trululu' AS n, 'Gomas suaves Trululu, dulces de gelatina con sabores frutales. Snack favorito de niños y adultos.\n\nGomas Trululu Frutas 80 gr' AS d, 2500.00 AS p, 80 AS s, 1 AS a, 8 AS c, 'https://loremflickr.com/400/400/gummy,candy?lock=811' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Trululu');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Pringles' AS n, 'Papas Pringles sabor original, crujientes y uniformes en su característico tubo.\n\nPringles Original 124 gr' AS d, 9500.00 AS p, 50 AS s, 1 AS a, 8 AS c, 'https://loremflickr.com/400/400/pringles,chips?lock=812' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Pringles');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Bocadillo' AS n, 'Bocadillo veleño tradicional colombiano de guayaba. Dulce típico para postres y onces.\n\nBocadillo Velez paquete x6' AS d, 4200.00 AS p, 60 AS s, 1 AS a, 8 AS c, 'https://loremflickr.com/400/400/guava,paste?lock=813' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Bocadillo');

-- Aseo (categoria_id=9)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Desodorante' AS n, 'Desodorante en barra antibacterial, protección 24 horas contra el sudor y mal olor.\n\nDesodorante Rexona Barra 50 gr' AS d, 8500.00 AS p, 50 AS s, 1 AS a, 9 AS c, 'https://loremflickr.com/400/400/deodorant?lock=910' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Desodorante');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Cepillo Dental' AS n, 'Cepillo dental con cerdas suaves, ideal para limpieza dental diaria sin dañar las encías.\n\nCepillo Colgate Premier x2' AS d, 6800.00 AS p, 70 AS s, 1 AS a, 9 AS c, 'https://loremflickr.com/400/400/toothbrush?lock=911' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Cepillo Dental');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Limpiador Multiusos' AS n, 'Limpiador líquido multiusos para superficies de la casa. Aroma a lavanda y poder desinfectante.\n\nFabuloso Lavanda 1000 ml' AS d, 8200.00 AS p, 45 AS s, 1 AS a, 9 AS c, 'https://loremflickr.com/400/400/cleaning,spray?lock=912' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Limpiador Multiusos');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Toallas Higienicas' AS n, 'Toallas higiénicas con cubierta seca, máxima protección y comodidad para cualquier momento.\n\nNosotras Natural x10' AS d, 8800.00 AS p, 50 AS s, 1 AS a, 9 AS c, 'https://loremflickr.com/400/400/feminine,hygiene?lock=913' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Toallas Higienicas');

-- Enlatados (categoria_id=10)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Frijoles en Lata' AS n, 'Frijoles cocidos listos para servir, con cerdo. Solución práctica y rápida para una comida.\n\nFrijoles La Constancia 410 gr' AS d, 6500.00 AS p, 55 AS s, 1 AS a, 10 AS c, 'https://loremflickr.com/400/400/canned,beans?lock=1010' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Frijoles en Lata');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Champinones' AS n, 'Champiñones laminados en lata, listos para usar en pastas, pizzas y guisos.\n\nChampiñones Laminados 184 gr' AS d, 5800.00 AS p, 45 AS s, 1 AS a, 10 AS c, 'https://loremflickr.com/400/400/mushrooms,canned?lock=1011' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Champinones');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Aceitunas' AS n, 'Aceitunas verdes rellenas con pimiento, snack mediterráneo perfecto para picadas y ensaladas.\n\nAceitunas verdes 250 gr' AS d, 7200.00 AS p, 40 AS s, 1 AS a, 10 AS c, 'https://loremflickr.com/400/400/green,olives?lock=1012' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Aceitunas');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT * FROM (SELECT 'Mayonesa' AS n, 'Mayonesa cremosa elaborada con aceite vegetal y huevo. Aderezo perfecto para sandwiches y ensaladas.\n\nMayonesa Fruco 380 gr' AS d, 6800.00 AS p, 60 AS s, 1 AS a, 10 AS c, 'https://loremflickr.com/400/400/mayonnaise?lock=1013' AS i, NOW() AS f) t
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Mayonesa');

-- Congelados (nueva, dinámicamente buscamos su id)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Helado Vainilla', 'Helado de vainilla cremoso, postre clásico ideal para cualquier ocasión.\n\nHelado Crem Helado Vainilla 1000 ml', 14500.00, 30, 1,
  (SELECT id FROM categorias WHERE nombre = 'Congelados'),
  'https://loremflickr.com/400/400/vanilla,icecream?lock=1110', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Helado Vainilla')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Congelados');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Pizza Congelada', 'Pizza congelada de pepperoni, lista para hornear. Una solución rápida para cualquier momento.\n\nPizza Maggi Pepperoni 350 gr', 13800.00, 35, 1,
  (SELECT id FROM categorias WHERE nombre = 'Congelados'),
  'https://loremflickr.com/400/400/frozen,pizza?lock=1111', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Pizza Congelada')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Congelados');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Papas Prefritas', 'Papas prefritas congeladas, listas para freír y servir. Crujientes por fuera y suaves por dentro.\n\nMcCain Papas a la Francesa 1000 gr', 12500.00, 50, 1,
  (SELECT id FROM categorias WHERE nombre = 'Congelados'),
  'https://loremflickr.com/400/400/french,fries,frozen?lock=1112', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Papas Prefritas')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Congelados');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Nuggets de Pollo', 'Nuggets de pollo congelados, snack favorito de los niños. Crujientes y deliciosos.\n\nPietran Nuggets 450 gr', 11800.00, 40, 1,
  (SELECT id FROM categorias WHERE nombre = 'Congelados'),
  'https://loremflickr.com/400/400/chicken,nuggets?lock=1113', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Nuggets de Pollo')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Congelados');

-- Dulces (nueva)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Chocolatina Jet', 'Chocolatina Jet de leche, clásico colombiano con álbum coleccionable.\n\nChocolatina Jet 12 gr', 800.00, 200, 1,
  (SELECT id FROM categorias WHERE nombre = 'Dulces'),
  'https://loremflickr.com/400/400/chocolate,bar?lock=1210', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Chocolatina Jet')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Dulces');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Bombones', 'Bombones surtidos de chocolate. Perfectos para regalar o disfrutar en momentos especiales.\n\nBombones Ferrero Rocher x16', 28500.00, 25, 1,
  (SELECT id FROM categorias WHERE nombre = 'Dulces'),
  'https://loremflickr.com/400/400/chocolate,bonbons?lock=1211', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Bombones')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Dulces');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Caramelos Bon Bon Bum', 'Bon Bon Bum, paleta clásica colombiana con chicle adentro. Sabor a fresa y otros.\n\nBon Bon Bum Surtido x24', 12500.00, 50, 1,
  (SELECT id FROM categorias WHERE nombre = 'Dulces'),
  'https://loremflickr.com/400/400/lollipop,candy?lock=1212', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Caramelos Bon Bon Bum')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Dulces');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Galletas Chips Ahoy', 'Galletas con chispas de chocolate, dulces y crujientes. Snack perfecto a cualquier hora.\n\nChips Ahoy 168 gr', 6500.00, 50, 1,
  (SELECT id FROM categorias WHERE nombre = 'Dulces'),
  'https://loremflickr.com/400/400/chocolate,chip,cookies?lock=1213', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Galletas Chips Ahoy')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Dulces');

-- Mascotas (nueva)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Concentrado Perro Adulto', 'Concentrado completo y balanceado para perros adultos de todas las razas. Sabor pollo.\n\nDog Chow Adulto 4 kg', 38500.00, 25, 1,
  (SELECT id FROM categorias WHERE nombre = 'Mascotas'),
  'https://loremflickr.com/400/400/dog,food,kibble?lock=1310', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Concentrado Perro Adulto')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Mascotas');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Concentrado Gato', 'Concentrado para gatos adultos con sabor a pescado. Nutrición balanceada y completa.\n\nWhiskas Adulto 1.5 kg', 22500.00, 30, 1,
  (SELECT id FROM categorias WHERE nombre = 'Mascotas'),
  'https://loremflickr.com/400/400/cat,food?lock=1311', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Concentrado Gato')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Mascotas');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Arena para Gato', 'Arena sanitaria absorbente para gatos. Control de olores y fácil de limpiar.\n\nArena Tidy Cats 4 kg', 18500.00, 35, 1,
  (SELECT id FROM categorias WHERE nombre = 'Mascotas'),
  'https://loremflickr.com/400/400/cat,litter?lock=1312', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Arena para Gato')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Mascotas');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Galletas para Perro', 'Galletas premiadoras para perros, snack saludable e ideal para entrenamiento.\n\nNutreCan Snacks 250 gr', 8500.00, 50, 1,
  (SELECT id FROM categorias WHERE nombre = 'Mascotas'),
  'https://loremflickr.com/400/400/dog,treats?lock=1313', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Galletas para Perro')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Mascotas');

-- Bebes (nueva)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Panales Etapa 3', 'Pañales desechables súper absorbentes etapa 3, para bebés de 7-11 kg. Comodidad total.\n\nHuggies Natural Care x36', 32500.00, 30, 1,
  (SELECT id FROM categorias WHERE nombre = 'Bebes'),
  'https://loremflickr.com/400/400/baby,diapers?lock=1410', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Panales Etapa 3')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Bebes');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Toallitas Humedas', 'Toallitas húmedas para bebés, sin alcohol ni fragancia, dermatológicamente probadas.\n\nHuggies Toallitas x80', 9500.00, 60, 1,
  (SELECT id FROM categorias WHERE nombre = 'Bebes'),
  'https://loremflickr.com/400/400/baby,wipes?lock=1411', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Toallitas Humedas')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Bebes');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Compota de Frutas', 'Compota de manzana 100% natural, sin azúcar agregada. Ideal a partir de los 6 meses.\n\nGerber Manzana 113 gr', 3800.00, 80, 1,
  (SELECT id FROM categorias WHERE nombre = 'Bebes'),
  'https://loremflickr.com/400/400/baby,food,jar?lock=1412', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Compota de Frutas')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Bebes');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Shampoo Bebe', 'Shampoo suave para bebés, fórmula sin lágrimas con aroma a manzanilla.\n\nJohnson Baby Shampoo 400 ml', 18500.00, 45, 1,
  (SELECT id FROM categorias WHERE nombre = 'Bebes'),
  'https://loremflickr.com/400/400/baby,shampoo?lock=1413', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Shampoo Bebe')
  AND EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Bebes');
