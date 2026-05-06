-- Nuevas categorias
INSERT INTO categorias (nombre) VALUES
('Bebidas'),
('Carnes'),
('Verduras'),
('Snacks'),
('Aseo'),
('Enlatados');

-- Productos Lacteos (categoria_id = 1)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, fecha_creacion) VALUES
('Yogurt Natural', 'Es un producto lacteo fermentado que se obtiene mediante la accion de bacterias lacticas sobre la leche, resultando en un alimento cremoso, ligeramente acido y altamente nutritivo.\n\nYogurt Alpina Natural 200 gr', 3200.00, 80, 1, 1, NOW()),
('Queso Campesino', 'Es un queso fresco colombiano, blanco, de textura firme y sabor suave, elaborado con leche de vaca pasteurizada. Es uno de los quesos mas consumidos en Colombia.\n\nQueso Campesino Colanta 350 gr', 9800.00, 45, 1, 1, NOW()),
('Crema de Leche', 'Es la parte grasa de la leche que se separa de forma natural o por centrifugacion. Se utiliza ampliamente en reposteria, salsas y preparaciones culinarias.\n\nCrema de Leche Alqueria 200 ml', 4500.00, 55, 1, 1, NOW()),
('Kumis', 'Es una bebida lactea fermentada de origen colombiano, con sabor dulce y ligeramente acido. Se elabora a partir de leche de vaca con cultivos lacticos especificos.\n\nKumis Alpina 200 ml', 2800.00, 65, 1, 1, NOW()),
('Queso Doble Crema', 'Es un queso colombiano semimaduro, de textura suave y fundente, ideal para gratinar, arepas y preparaciones calientes. Tiene un sabor cremoso y ligeramente salado.\n\nQueso Doble Crema Colanta 250 gr', 8500.00, 40, 1, 1, NOW());

-- Productos Granos (categoria_id = 2)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, fecha_creacion) VALUES
('Azucar', 'Es un endulzante natural obtenido de la cana de azucar mediante procesos de molienda, evaporacion y cristalizacion. Es un carbohidrato puro que proporciona energia rapida.\n\nAzucar Manuelita 1000 gr', 4200.00, 90, 1, 2, NOW()),
('Avena', 'Es un cereal integral altamente nutritivo, rico en fibra soluble, proteinas y minerales. Se consume en hojuelas para preparar bebidas, coladas y como ingrediente en reposteria.\n\nAvena en Hojuelas Quaker 380 gr', 5600.00, 60, 1, 2, NOW()),
('Pasta Espagueti', 'Es un tipo de pasta larga y delgada de origen italiano, elaborada con harina de trigo y agua. Es uno de los formatos de pasta mas populares y versatiles en la cocina.\n\nPasta Espagueti Doria 500 gr', 3800.00, 75, 1, 2, NOW()),
('Harina de Trigo', 'Es un polvo fino obtenido de la molienda del grano de trigo. Es el ingrediente base para la elaboracion de panes, tortas, galletas y muchas otras preparaciones.\n\nHarina de Trigo Haz de Oros 1000 gr', 4100.00, 50, 1, 2, NOW()),
('Maiz Pira', 'Son granos de maiz especiales que al calentarse explotan formando palomitas. Es un snack natural rico en fibra, bajo en calorias y muy popular para consumo familiar.\n\nMaiz Pira Diana 500 gr', 3500.00, 55, 1, 2, NOW());

-- Productos Frutas (categoria_id = 3)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, fecha_creacion) VALUES
('Banano', 'Es una fruta tropical alargada y curva, de cascara amarilla y pulpa suave y dulce. Es rica en potasio, vitamina B6 y fibra. Es una de las frutas mas consumidas en Colombia.\n\nBanano criollo por libra', 1800.00, 100, 1, 3, NOW()),
('Manzana Roja', 'Es una fruta de piel roja brillante, pulpa blanca y crujiente, con un sabor dulce y ligeramente acido. Es rica en fibra, vitamina C y antioxidantes.\n\nManzana Roja importada por unidad', 2500.00, 70, 1, 3, NOW()),
('Naranja', 'Es un citrico esferico de cascara anaranjada y pulpa jugosa, con alto contenido de vitamina C y fibra. Se consume fresca o en jugo y es fundamental en la dieta diaria.\n\nNaranja Valencia por libra', 2200.00, 85, 1, 3, NOW()),
('Limon', 'Es un citrico pequeno, de forma ovalada, cascara verde y pulpa acida muy jugosa. Es indispensable en la cocina colombiana para jugos, aderezos, limonada y sazon.\n\nLimon Tahiti por libra', 3000.00, 90, 1, 3, NOW()),
('Pina', 'Es una fruta tropical grande con cascara rugosa y corona de hojas verdes. Su pulpa es amarilla, jugosa, dulce y ligeramente acida, rica en vitamina C y bromelina.\n\nPina Gold por unidad', 5500.00, 35, 1, 3, NOW()),
('Fresa', 'Es una fruta pequena, roja y aromatica con pequenas semillas en su superficie. Tiene un sabor dulce y ligeramente acido, rica en vitamina C, manganeso y antioxidantes.\n\nFresa nacional bandeja 500 gr', 6800.00, 40, 1, 3, NOW());

-- Productos Panaderia (categoria_id = 4)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, fecha_creacion) VALUES
('Pan Tajado', 'Es un pan de molde blanco, suave y esponjoso, cortado en rebanadas uniformes. Es ideal para sandwiches, tostadas y acompanamiento de comidas.\n\nPan Tajado Bimbo 600 gr', 7200.00, 40, 1, 4, NOW()),
('Mogolla', 'Es un pan redondo tradicional colombiano, de corteza dorada y miga suave. Es uno de los panes mas populares para el desayuno y las onces en Colombia.\n\nMogolla tradicional paquete x6', 3500.00, 50, 1, 4, NOW()),
('Pan Integral', 'Es un pan elaborado con harina de trigo integral que conserva el salvado y el germen del grano, aportando mas fibra, vitaminas y minerales que el pan blanco.\n\nPan Integral Bimbo 480 gr', 8500.00, 35, 1, 4, NOW()),
('Galletas de Soda', 'Son galletas crujientes, ligeramente saladas, elaboradas con harina de trigo. Son versatiles para acompanar sopas, untarles queso, mantequilla o comerlas solas.\n\nGalletas de Soda Noel 300 gr', 4200.00, 60, 1, 4, NOW()),
('Bunuelo', 'Es un pan frito tipico colombiano, esferico, dorado y esponjoso, elaborado con queso fresco, almidon de yuca y huevo. Es un acompanamiento clasico del desayuno y las fiestas.\n\nBunuelo artesanal paquete x5', 5000.00, 30, 1, 4, NOW());

-- Productos Bebidas (categoria_id = 5)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, fecha_creacion) VALUES
('Agua', 'Es agua purificada y envasada para consumo humano, libre de contaminantes. Es esencial para la hidratacion diaria y la mas saludable de todas las bebidas.\n\nAgua Cristal 600 ml', 1500.00, 120, 1, 5, NOW()),
('Gaseosa', 'Es una bebida carbonatada con sabor dulce, elaborada con agua, gas carbonico, azucar y saborizantes. Es una de las bebidas mas populares para acompanar comidas.\n\nCoca-Cola 400 ml', 2800.00, 100, 1, 5, NOW()),
('Jugo de Naranja', 'Es una bebida elaborada a partir de concentrado de naranja, con vitamina C y sabor citrico refrescante. Ideal para el desayuno y como acompanamiento de comidas.\n\nJugo Hit Naranja 1000 ml', 4500.00, 65, 1, 5, NOW()),
('Cafe', 'Es una bebida preparada a partir de los granos tostados y molidos de la planta del cafe. Colombia es reconocida mundialmente por la calidad de su cafe suave y aromatico.\n\nCafe Sello Rojo 500 gr', 15800.00, 45, 1, 5, NOW()),
('Chocolate', 'Es una bebida tradicional colombiana que se prepara disolviendo pastillas de chocolate en leche caliente. Es un elemento central del desayuno y las onces colombianas.\n\nChocolate Corona 500 gr', 9200.00, 50, 1, 5, NOW()),
('Cerveza', 'Es una bebida alcoholica fermentada elaborada a partir de cebada malteada, lupulo, levadura y agua. Es la bebida alcoholica mas consumida en Colombia.\n\nCerveza Poker 330 ml', 3200.00, 80, 1, 5, NOW());

-- Productos Carnes (categoria_id = 6)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, fecha_creacion) VALUES
('Pechuga de Pollo', 'Es un corte magro de carne de pollo, bajo en grasa y alto en proteinas. Es uno de los cortes mas versatiles y consumidos, ideal para asar, hornear o guisar.\n\nPechuga de pollo por libra', 11500.00, 40, 1, 6, NOW()),
('Carne Molida', 'Es carne de res finamente picada, versatil para preparar bolonesa, empanadas, hamburguesas, albondigas y muchas otras recetas. Es un basico de la cocina colombiana.\n\nCarne molida de res por libra', 14200.00, 35, 1, 6, NOW()),
('Chorizo', 'Es un embutido colombiano elaborado con carne de cerdo y res, sazonado con especias, cebolla y ajo. Es un acompanamiento clasico de las parrilladas y bandeja paisa.\n\nChorizo antioqueno paquete x5', 9800.00, 50, 1, 6, NOW()),
('Huevos', 'Son un alimento basico, fuente de proteinas de alta calidad, vitaminas y minerales. Los huevos de gallina son uno de los ingredientes mas utilizados en la cocina colombiana.\n\nHuevos rojos x30 unidades', 18500.00, 30, 1, 6, NOW()),
('Salchicha', 'Es un embutido procesado elaborado con carne de pollo, cerdo o res, condimentos y especias. Es muy popular en perros calientes, salchipapas y desayunos.\n\nSalchicha Zenu paquete x10', 7600.00, 55, 1, 6, NOW());

-- Productos Verduras (categoria_id = 7)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, fecha_creacion) VALUES
('Tomate', 'Es una hortaliza de fruto rojo, carnoso y jugoso, fundamental en la cocina colombiana para guisos, ensaladas, salsas y hogao. Es rico en licopeno y vitamina C.\n\nTomate chonto por libra', 2800.00, 80, 1, 7, NOW()),
('Cebolla Cabezona', 'Es una hortaliza de bulbo redondo con capas concentricas, de sabor fuerte que se suaviza al cocinarla. Es base del hogao y sofrito colombiano.\n\nCebolla cabezona blanca por libra', 3200.00, 70, 1, 7, NOW()),
('Papa', 'Es un tuberculo originario de Sudamerica, base de la alimentacion colombiana. Se consume en sopas, guisos, fritos, al horno y en infinidad de preparaciones.\n\nPapa pastusa por libra', 2400.00, 100, 1, 7, NOW()),
('Zanahoria', 'Es una raiz comestible de color naranja, crujiente y ligeramente dulce. Es rica en betacaroteno, vitamina A y fibra. Se usa en ensaladas, sopas, jugos y guisos.\n\nZanahoria por libra', 2100.00, 75, 1, 7, NOW()),
('Aguacate', 'Es un fruto de pulpa verde cremosa, sabor suave y alto contenido de grasas saludables, potasio y fibra. En Colombia se consume con todo: sopas, arroz, ensaladas y como acompanamiento.\n\nAguacate Hass por unidad', 4500.00, 45, 1, 7, NOW()),
('Platano', 'Es un fruto tropical de la familia de las musaceas, mas grande y firme que el banano. En Colombia se consume verde (para patacones y sancocho) o maduro (frito o al horno).\n\nPlatano harton verde por unidad', 1200.00, 90, 1, 7, NOW());

-- Productos Snacks (categoria_id = 8)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, fecha_creacion) VALUES
('Papas Fritas', 'Son rodajas finas de papa fritas hasta quedar crujientes, sazonadas con sal u otros sabores. Son el snack mas popular y un clasico acompanamiento colombiano.\n\nPapas Margarita Natural 115 gr', 4800.00, 70, 1, 8, NOW()),
('Chocorramo', 'Es un iconico ponque colombiano cubierto de chocolate, con bizcocho esponjoso y relleno de arequipe. Es el snack mas emblematico de Colombia desde 1972.\n\nChocorramo Ramo 65 gr', 2200.00, 100, 1, 8, NOW()),
('Galletas Oreo', 'Son galletas tipo sandwich con dos tapas de chocolate oscuro y un relleno cremoso de vainilla. Son una de las galletas mas populares y reconocidas mundialmente.\n\nGalletas Oreo Original 108 gr', 3500.00, 60, 1, 8, NOW()),
('Mani Salado', 'Es un fruto seco tostado y sazonado con sal, rico en proteinas, grasas saludables y fibra. Es un snack nutritivo y energetico ideal para cualquier momento.\n\nMani salado La Especial 200 gr', 5200.00, 55, 1, 8, NOW()),
('Choclitos', 'Son snacks de maiz con forma de triangulo y sabor a limon, crujientes y adictivos. Son uno de los pasabocas mas queridos en Colombia.\n\nChoclitos Limon Frito Lay 230 gr', 6500.00, 50, 1, 8, NOW());

-- Productos Aseo (categoria_id = 9)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, fecha_creacion) VALUES
('Jabon de Bano', 'Es un producto de higiene personal elaborado con grasas, aceites y fragancia, disenado para limpiar e hidratar la piel durante el bano diario.\n\nJabon Dove Original 90 gr x3', 9800.00, 50, 1, 9, NOW()),
('Shampoo', 'Es un producto de cuidado capilar que limpia el cuero cabelludo y el cabello, eliminando grasa, suciedad y residuos de productos. Fortalece y da brillo al cabello.\n\nShampoo Head & Shoulders 375 ml', 18500.00, 35, 1, 9, NOW()),
('Papel Higienico', 'Es un producto esencial de higiene personal, elaborado con fibras de celulosa suaves y absorbentes. Es un articulo de primera necesidad en todo hogar.\n\nPapel Higienico Familia x12 rollos', 16200.00, 40, 1, 9, NOW()),
('Detergente', 'Es un producto de limpieza disenado para eliminar manchas y suciedad de la ropa. Se disuelve en agua y actua sobre las fibras textiles durante el lavado.\n\nDetergente Ariel 2000 gr', 22500.00, 30, 1, 9, NOW()),
('Crema Dental', 'Es un producto de higiene bucal en forma de pasta que se usa con el cepillo de dientes para limpiar, proteger el esmalte y prevenir caries y enfermedades de las encias.\n\nCrema Dental Colgate Triple Accion 150 ml', 7800.00, 60, 1, 9, NOW()),
('Jabon Lavavajillas', 'Es un producto de limpieza concentrado disenado para eliminar grasa y residuos de alimentos de platos, vasos, ollas y utensilios de cocina.\n\nLavavajillas Axion Limon 450 gr', 5200.00, 55, 1, 9, NOW());

-- Productos Enlatados (categoria_id = 10)
INSERT INTO productos (nombre, descripcion, precio, stock, activo, categoria_id, fecha_creacion) VALUES
('Atun', 'Es pescado de atun cocido y envasado en lata con aceite o agua. Es una fuente practica de proteinas, omega 3 y minerales. Muy usado en ensaladas, sandwiches y arroz.\n\nAtun Van Camps en aceite 160 gr', 6800.00, 65, 1, 10, NOW()),
('Sardinas', 'Son pequenos pescados enlatados en salsa de tomate o aceite, ricos en omega 3, calcio y proteinas. Son un alimento economico, nutritivo y de larga conservacion.\n\nSardinas Van Camps en salsa de tomate 170 gr', 5200.00, 55, 1, 10, NOW()),
('Salsa de Tomate', 'Es una salsa elaborada a partir de tomates maduros, vinagre, azucar y especias. Es el condimento mas popular para acompanar papas fritas, hamburguesas y empanadas.\n\nSalsa de Tomate Fruco 400 gr', 5800.00, 60, 1, 10, NOW()),
('Maiz Tierno', 'Son granos de maiz dulce tierno, cocidos y envasados. Se usan como ingrediente en ensaladas, arepas, sopas y como acompanamiento de diversos platos.\n\nMaiz Tierno Del Monte 241 gr', 4500.00, 50, 1, 10, NOW()),
('Salchicha Enlatada', 'Son salchichas cocidas y conservadas en salmuera dentro de una lata sellada. Son practicas, de larga duracion y listas para calentar y consumir.\n\nSalchichas Zenu en lata x10', 8900.00, 40, 1, 10, NOW());
