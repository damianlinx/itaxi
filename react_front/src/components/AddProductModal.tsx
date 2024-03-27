import React, { useState } from 'react';
import { Modal, Button, Form, Alert } from 'react-bootstrap';
import Props from '../models/Props';
import { addProduct } from '../services/productService.ts';

const AddProductModal: React.FC<Props> = ({ show, onClose }) => {
  const [productName, setProductName] = useState('');
  const [productPrice, setProductPrice] = useState('');
  const [errors, setErrors] = useState({ productName: '', productPrice: '' });
  const [errorMessage, setErrorMessage] = useState('');

  const clearFormFields = () => {
    setProductName('');
    setProductPrice('');
    setErrors({ productName: '', productPrice: '' });
    setErrorMessage('');
  };

  const handleProductNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setProductName(event.target.value);
    setErrors({ ...errors, productName: '' });
    setErrorMessage('');
  };

  const handleProductPriceChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = event.target;
    // Sprawdź czy wprowadzany znak jest cyfrą lub kropką
    if (/^\d*\.?\d*$/.test(value)) {
      setProductPrice(value);
      setErrors({ ...errors, productPrice: '' });
      setErrorMessage('');
    }
  };

  const handleAddProduct = async () => {
    try {
      if (!productName.trim()) {
        setErrors({ ...errors, productName: 'Nazwa produktu jest wymagana' });
        return;
      }
  
      if (!productPrice.trim() || isNaN(Number(productPrice))) {
        setErrors({ ...errors, productPrice: 'Cena produktu musi być liczbą' });
        return;
      }
  
      const error = await addProduct(productName, parseFloat(productPrice));
      if (error) {
        setErrorMessage(error);
      } else {
        clearFormFields();
        onClose();
      }
    } catch (error) {
      console.error('Error adding product:', error);
      setErrorMessage('Wystąpił nieoczekiwany błąd');
    }
  };

  return (
    <Modal show={show} onHide={() => { onClose(); clearFormFields(); }}>
      <Modal.Header closeButton>
        <Modal.Title>Dodaj produkt</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}
        <Form>
          <Form.Group controlId="productName">
            <Form.Label>Nazwa produktu:</Form.Label>
            <Form.Control
              type="text"
              value={productName}
              onChange={handleProductNameChange}
              isInvalid={!!errors.productName}
            />
            <Form.Control.Feedback type="invalid">{errors.productName}</Form.Control.Feedback>
          </Form.Group>
          <Form.Group controlId="productPrice">
            <Form.Label>Cena produktu:</Form.Label>
            <Form.Control
              type="text"
              value={productPrice}
              onChange={handleProductPriceChange}
              isInvalid={!!errors.productPrice}
            />
            <Form.Control.Feedback type="invalid">{errors.productPrice}</Form.Control.Feedback>
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={() => { onClose(); clearFormFields(); }}>Anuluj</Button>
        <Button variant="primary" onClick={handleAddProduct}>Dodaj produkt</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default AddProductModal;