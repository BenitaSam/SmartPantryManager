require('dotenv').config();
const express = require('express');
const cors = require('cors');
const pool = require('./db');

const app = express();
app.use(cors());
app.use(express.json());

const UNITS = ['g', 'kg', 'ml', 'l', 'pcs'];

//make json shape same for every query
const PANTRY_COLS = `id, name, quantity::float AS quantity, unit,
                     to_char(expiry_date, 'YYYY-MM-DD') AS expiry_date`;

//validate data sent by app like if name is filled, quant >0 else error message
//server side validation
function validatePantry(body) {
  const errors = [];
  if (!body.name || !String(body.name).trim()) errors.push('Name is required');
  const qty = Number(body.quantity);
  if (!(qty > 0)) errors.push('Quantity must be greater than 0');
  if (!UNITS.includes(body.unit)) errors.push('Unit must be one of: ' + UNITS.join(', '));
  if (body.expiry_date && !/^\d{4}-\d{2}-\d{2}$/.test(body.expiry_date))
    errors.push('Expiry date must be YYYY-MM-DD');
  return errors;
}

//////////////////crud blocks

//read
app.get('/pantry', async (req, res) => {
  try {
    const r = await pool.query(`SELECT ${PANTRY_COLS} FROM pantry_items ORDER BY name`);
    res.json(r.rows);
  } catch (e) { console.error(e); res.status(500).json({ error: 'Database error' }); }
});

//create
app.post('/pantry', async (req, res) => {
  const errors = validatePantry(req.body);
  if (errors.length) return res.status(400).json({ errors });
  try {
    const { name, quantity, unit, expiry_date } = req.body;
    const r = await pool.query(
      `INSERT INTO pantry_items (name, quantity, unit, expiry_date)
       VALUES ($1, $2, $3, $4) RETURNING ${PANTRY_COLS}`,
      [name.trim().toLowerCase(), quantity, unit, expiry_date || null]
    );
    res.status(201).json(r.rows[0]);
  } catch (e) { console.error(e); res.status(500).json({ error: 'Database error' }); }
});

//update
app.put('/pantry/:id', async (req, res) => {
  const errors = validatePantry(req.body);
  if (errors.length) return res.status(400).json({ errors });
  try {
    const { name, quantity, unit, expiry_date } = req.body;
    const r = await pool.query(
      `UPDATE pantry_items SET name=$1, quantity=$2, unit=$3, expiry_date=$4
       WHERE id=$5 RETURNING ${PANTRY_COLS}`,
      [name.trim().toLowerCase(), quantity, unit, expiry_date || null, req.params.id]
    );
    if (r.rowCount === 0) return res.status(404).json({ error: 'Item not found' });
    res.json(r.rows[0]);
  } catch (e) { console.error(e); res.status(500).json({ error: 'Database error' }); }
});

//delete
app.delete('/pantry/:id', async (req, res) => {
  try {
    const r = await pool.query('DELETE FROM pantry_items WHERE id=$1', [req.params.id]);
    if (r.rowCount === 0) return res.status(404).json({ error: 'Item not found' });
    res.status(204).send();
  } catch (e) { console.error(e); res.status(500).json({ error: 'Database error' }); }
});









//fetches recipes and their corresponding ingredients and returns it in different ways in a json arr
const RECIPE_SQL = `SELECT r.id, r.name, r.steps, COALESCE
                    (json_agg(json_build_object(
                    'name', ri.name, 'quantity', ri.quantity::float, 'unit', ri.unit) 
                    ORDER BY ri.id) FILTER (WHERE ri.id IS NOT NULL), '[]') 
                    AS ingredients
                    FROM recipes r
                    LEFT JOIN recipe_ingredients ri ON ri.recipe_id = r.id`;

app.get('/recipes', async (req, res) => {
  try {
    const r = await pool.query(`${RECIPE_SQL} GROUP BY r.id ORDER BY r.id`);
    res.json(r.rows);
  } catch (e) { console.error(e); res.status(500).json({ error: 'Database error' }); }
});

app.get('/recipes/:id', async (req, res) => {
  try {
    const r = await pool.query(`${RECIPE_SQL} WHERE r.id=$1 GROUP BY r.id`, [req.params.id]);
    if (r.rowCount === 0) return res.status(404).json({ error: 'Recipe not found' });
    res.json(r.rows[0]);
  } catch (e) { console.error(e); res.status(500).json({ error: 'Database error' }); }
});


//lets phone emulator reach the db server on  the port
const PORT = process.env.PORT || 3000;
app.listen(PORT, '0.0.0.0', () => console.log(`Pantry API running on port ${PORT}`));