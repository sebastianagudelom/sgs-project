-- =====================================================
-- SGS · Fix de categoria_id de productos por nombre
-- Reasocia cada producto a su categoría correcta usando
-- una lookup por nombre. Idempotente.
-- =====================================================

-- Helper: para cada producto, asignar categoria_id = id_de_categoria_por_nombre

-- Lácteos
UPDATE productos SET categoria_id = (SELECT id FROM categorias WHERE nombre='Lacteos') WHERE nombre IN
  ('Yogurt Natural','Queso Campesino','Crema de Leche','Kumis','Queso Doble Crema',
   'Leche Entera','Mantequilla','Yogurt Griego','Arequipe');

-- Granos
UPDATE productos SET categoria_id = (SELECT id FROM categorias WHERE nombre='Granos') WHERE nombre IN
  ('Azucar','Avena','Pasta Espagueti','Harina de Trigo','Maiz Pira',
   'Arroz Blanco','Frijol Rojo','Lentejas','Garbanzos','Sal','Aceite Vegetal');

-- Frutas
UPDATE productos SET categoria_id = (SELECT id FROM categorias WHERE nombre='Frutas') WHERE nombre IN
  ('Banano','Manzana Roja','Naranja','Limon','Pina','Fresa',
   'Mango','Uvas','Sandia','Papaya','Mora','Maracuya');

-- Panadería
UPDATE productos SET categoria_id = (SELECT id FROM categorias WHERE nombre='Panaderia') WHERE nombre IN
  ('Pan Tajado','Mogolla','Pan Integral','Galletas de Soda','Bunuelo');

-- Bebidas
UPDATE productos SET categoria_id = (SELECT id FROM categorias WHERE nombre='Bebidas') WHERE nombre IN
  ('Agua','Gaseosa','Jugo de Naranja','Cafe','Chocolate','Cerveza',
   'Te Verde','Bebida Energetica','Vino Tinto','Aromatica');

-- Carnes
UPDATE productos SET categoria_id = (SELECT id FROM categorias WHERE nombre='Carnes') WHERE nombre IN
  ('Pechuga de Pollo','Carne Molida','Chorizo','Huevos','Salchicha',
   'Lomo de Res','Costilla de Cerdo','Tilapia','Jamon','Tocineta');

-- Verduras
UPDATE productos SET categoria_id = (SELECT id FROM categorias WHERE nombre='Verduras') WHERE nombre IN
  ('Tomate','Cebolla Cabezona','Papa','Zanahoria','Aguacate','Platano',
   'Lechuga','Pepino Cohombro','Pimenton','Brocoli','Espinaca','Yuca');

-- Snacks
UPDATE productos SET categoria_id = (SELECT id FROM categorias WHERE nombre='Snacks') WHERE nombre IN
  ('Papas Fritas','Chocorramo','Galletas Oreo','Mani Salado','Choclitos',
   'Doritos','Trululu','Pringles','Bocadillo');

-- Aseo
UPDATE productos SET categoria_id = (SELECT id FROM categorias WHERE nombre='Aseo') WHERE nombre IN
  ('Jabon de Bano','Shampoo','Papel Higienico','Detergente','Crema Dental','Jabon Lavavajillas',
   'Desodorante','Cepillo Dental','Limpiador Multiusos','Toallas Higienicas');

-- Enlatados
UPDATE productos SET categoria_id = (SELECT id FROM categorias WHERE nombre='Enlatados') WHERE nombre IN
  ('Atun','Sardinas','Salsa de Tomate','Maiz Tierno','Salchicha Enlatada',
   'Frijoles en Lata','Champinones','Aceitunas','Mayonesa');

-- Congelados
UPDATE productos SET categoria_id = (SELECT id FROM categorias WHERE nombre='Congelados') WHERE nombre IN
  ('Helado Vainilla','Pizza Congelada','Papas Prefritas','Nuggets de Pollo');

-- Dulces
UPDATE productos SET categoria_id = (SELECT id FROM categorias WHERE nombre='Dulces') WHERE nombre IN
  ('Chocolatina Jet','Bombones','Caramelos Bon Bon Bum','Galletas Chips Ahoy');

-- Mascotas
UPDATE productos SET categoria_id = (SELECT id FROM categorias WHERE nombre='Mascotas') WHERE nombre IN
  ('Concentrado Perro Adulto','Concentrado Gato','Arena para Gato','Galletas para Perro');

-- Bebés
UPDATE productos SET categoria_id = (SELECT id FROM categorias WHERE nombre='Bebes') WHERE nombre IN
  ('Panales Etapa 3','Toallitas Humedas','Compota de Frutas','Shampoo Bebe');
