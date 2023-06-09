import React from 'react';
import ReactDOM from 'react-dom/client';
import './style.css';
import Router from './router.jsx';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Router/>
  </React.StrictMode>
);