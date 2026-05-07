-- =====================================================
-- SGS · Completar productos faltantes
-- Inserta solo los productos que NO existen en BD,
-- usando el categoria_id correcto (lookup por nombre)
-- =====================================================

-- Helper: variables para IDs reales de categorías
SET @cat_verduras  = (SELECT id FROM categorias WHERE nombre='Verduras');
SET @cat_snacks    = (SELECT id FROM categorias WHERE nombre='Snacks');
SET @cat_aseo      = (SELECT id FROM categorias WHERE nombre='Aseo');
SET @cat_enlatados = (SELECT id FROM categorias WHERE nombre='Enlatados');

-- ---------- Verduras (originales que faltan) ----------
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Tomate','Es una hortaliza de fruto rojo, carnoso y jugoso, fundamental en la cocina colombiana para guisos, ensaladas, salsas y hogao. Es rico en licopeno y vitamina C.\n\nTomate chonto por libra',
       2800.00, 80, 1, @cat_verduras, 'https://loremflickr.com/400/400/tomato?lock=71', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Tomate');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Cebolla Cabezona','Es una hortaliza de bulbo redondo con capas concentricas, de sabor fuerte que se suaviza al cocinarla. Es base del hogao y sofrito colombiano.\n\nCebolla cabezona blanca por libra',
       3200.00, 70, 1, @cat_verduras, 'https://loremflickr.com/400/400/onion?lock=72', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Cebolla Cabezona');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Papa','Es un tuberculo originario de Sudamerica, base de la alimentacion colombiana. Se consume en sopas, guisos, fritos, al horno y en infinidad de preparaciones.\n\nPapa pastusa por libra',
       2400.00, 100, 1, @cat_verduras, 'https://loremflickr.com/400/400/potato?lock=73', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Papa');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Zanahoria','Es una raiz comestible de color naranja, crujiente y ligeramente dulce. Es rica en betacaroteno, vitamina A y fibra. Se usa en ensaladas, sopas, jugos y guisos.\n\nZanahoria por libra',
       2100.00, 75, 1, @cat_verduras, 'https://loremflickr.com/400/400/carrot?lock=74', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Zanahoria');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Aguacate','Es un fruto de pulpa verde cremosa, sabor suave y alto contenido de grasas saludables, potasio y fibra.\n\nAguacate Hass por unidad',
       4500.00, 45, 1, @cat_verduras, 'https://loremflickr.com/400/400/avocado?lock=75', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Aguacate');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Platano','Es un fruto tropical de la familia de las musaceas, mas grande y firme que el banano. Se consume verde (para patacones) o maduro (frito).\n\nPlatano harton verde por unidad',
       1200.00, 90, 1, @cat_verduras, 'https://loremflickr.com/400/400/plantain,green?lock=76', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Platano');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Lechuga','Lechuga crespa fresca, base de ensaladas y sandwiches. Crujiente y saludable.\n\nLechuga crespa por unidad',
       2200.00, 60, 1, @cat_verduras, 'https://loremflickr.com/400/400/lettuce?lock=710', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Lechuga');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Pepino Cohombro','Pepino cohombro fresco, refrescante y ligero. Ideal para ensaladas, jugos y aguas saborizadas.\n\nPepino cohombro por libra',
       2500.00, 70, 1, @cat_verduras, 'https://loremflickr.com/400/400/cucumber?lock=711', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Pepino Cohombro');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Pimenton','Pimentón rojo fresco, dulce y crujiente. Ideal para guisos, hogao, ensaladas y salsas.\n\nPimentón rojo por libra',
       3800.00, 55, 1, @cat_verduras, 'https://loremflickr.com/400/400/red,bell,pepper?lock=712', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Pimenton');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Brocoli','Brócoli fresco, vegetal nutritivo lleno de vitaminas y antioxidantes. Ideal al vapor o salteado.\n\nBrócoli por unidad',
       4500.00, 40, 1, @cat_verduras, 'https://loremflickr.com/400/400/broccoli?lock=713', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Brocoli');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Espinaca','Espinaca fresca en hojas, rica en hierro, ácido fólico y vitaminas. Versátil en ensaladas y guisos.\n\nEspinaca paquete 250 gr',
       3500.00, 45, 1, @cat_verduras, 'https://loremflickr.com/400/400/spinach?lock=714', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Espinaca');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Yuca','Yuca fresca, tubérculo tradicional colombiano. Indispensable en sancochos y patacones.\n\nYuca por libra',
       2200.00, 80, 1, @cat_verduras, 'https://loremflickr.com/400/400/cassava,yuca?lock=715', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Yuca');

-- ---------- Snacks (originales) ----------
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Papas Fritas','Son rodajas finas de papa fritas hasta quedar crujientes, sazonadas con sal u otros sabores.\n\nPapas Margarita Natural 115 gr',
       4800.00, 70, 1, @cat_snacks, 'https://loremflickr.com/400/400/potato,chips?lock=81', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Papas Fritas');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Chocorramo','Iconico ponque colombiano cubierto de chocolate, con bizcocho esponjoso y relleno de arequipe.\n\nChocorramo Ramo 65 gr',
       2200.00, 100, 1, @cat_snacks, 'https://loremflickr.com/400/400/chocolate,cake,bar?lock=82', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Chocorramo');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Galletas Oreo','Galletas tipo sandwich con dos tapas de chocolate oscuro y un relleno cremoso de vainilla.\n\nGalletas Oreo Original 108 gr',
       3500.00, 60, 1, @cat_snacks, 'https://loremflickr.com/400/400/oreo,cookies?lock=83', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Galletas Oreo');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Mani Salado','Fruto seco tostado y sazonado con sal, rico en proteinas, grasas saludables y fibra.\n\nMani salado La Especial 200 gr',
       5200.00, 55, 1, @cat_snacks, 'https://loremflickr.com/400/400/peanuts?lock=84', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Mani Salado');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Choclitos','Snacks de maiz con forma de triangulo y sabor a limon, crujientes y adictivos.\n\nChoclitos Limon Frito Lay 230 gr',
       6500.00, 50, 1, @cat_snacks, 'https://loremflickr.com/400/400/corn,chips?lock=85', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Choclitos');

-- ---------- Aseo (originales) ----------
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Jabon de Bano','Producto de higiene personal elaborado con grasas, aceites y fragancia, disenado para limpiar e hidratar la piel.\n\nJabon Dove Original 90 gr x3',
       9800.00, 50, 1, @cat_aseo, 'https://loremflickr.com/400/400/soap,bar?lock=91', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Jabon de Bano');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Shampoo','Producto de cuidado capilar que limpia el cuero cabelludo y el cabello.\n\nShampoo Head & Shoulders 375 ml',
       18500.00, 35, 1, @cat_aseo, 'https://loremflickr.com/400/400/shampoo,bottle?lock=92', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Shampoo');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Papel Higienico','Producto esencial de higiene personal, elaborado con fibras de celulosa suaves y absorbentes.\n\nPapel Higienico Familia x12 rollos',
       16200.00, 40, 1, @cat_aseo, 'https://loremflickr.com/400/400/toilet,paper?lock=93', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Papel Higienico');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Detergente','Producto de limpieza disenado para eliminar manchas y suciedad de la ropa.\n\nDetergente Ariel 2000 gr',
       22500.00, 30, 1, @cat_aseo, 'https://loremflickr.com/400/400/laundry,detergent?lock=94', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Detergente');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Crema Dental','Producto de higiene bucal en forma de pasta que se usa con el cepillo de dientes.\n\nCrema Dental Colgate Triple Accion 150 ml',
       7800.00, 60, 1, @cat_aseo, 'https://loremflickr.com/400/400/toothpaste?lock=95', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Crema Dental');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Jabon Lavavajillas','Producto de limpieza concentrado disenado para eliminar grasa y residuos de alimentos de platos.\n\nLavavajillas Axion Limon 450 gr',
       5200.00, 55, 1, @cat_aseo, 'https://loremflickr.com/400/400/dish,soap?lock=96', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Jabon Lavavajillas');

-- ---------- Enlatados (originales) ----------
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Atun','Pescado de atun cocido y envasado en lata con aceite o agua. Fuente practica de proteinas y omega 3.\n\nAtun Van Camps en aceite 160 gr',
       6800.00, 65, 1, @cat_enlatados, 'https://loremflickr.com/400/400/canned,tuna?lock=101', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Atun');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Sardinas','Pequenos pescados enlatados en salsa de tomate o aceite, ricos en omega 3, calcio y proteinas.\n\nSardinas Van Camps en salsa de tomate 170 gr',
       5200.00, 55, 1, @cat_enlatados, 'https://loremflickr.com/400/400/sardines,can?lock=102', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Sardinas');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Salsa de Tomate','Salsa elaborada a partir de tomates maduros, vinagre, azucar y especias.\n\nSalsa de Tomate Fruco 400 gr',
       5800.00, 60, 1, @cat_enlatados, 'https://loremflickr.com/400/400/ketchup,bottle?lock=103', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Salsa de Tomate');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Maiz Tierno','Granos de maiz dulce tierno, cocidos y envasados.\n\nMaiz Tierno Del Monte 241 gr',
       4500.00, 50, 1, @cat_enlatados, 'https://loremflickr.com/400/400/canned,corn?lock=104', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Maiz Tierno');

INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, imagen_url, fecha_creacion)
SELECT 'Salchicha Enlatada','Salchichas cocidas y conservadas en salmuera dentro de una lata sellada.\n\nSalchichas Zenu en lata x10',
       8900.00, 40, 1, @cat_enlatados, 'https://loremflickr.com/400/400/canned,sausages?lock=105', NOW()
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Salchicha Enlatada');
