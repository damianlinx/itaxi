import React, { useState, useEffect } from 'react';
import { Button, Table } from 'react-bootstrap';
import Product from '../models/Product';
import { fetchProducts, deleteProduct } from '../services/productService.ts';
import AddProductModal from './AddProductModal.tsx';

const ProductList: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [showAddProductModal, setShowAddProductModal] = useState(false);

  useEffect(() => {
    fetchProductData();
  }, []);

  const fetchProductData = async () => {
    const data = await fetchProducts();
    setProducts(data);
  };

  const handleAddProductClick = () => {
    setShowAddProductModal(true);
  };

  const handleDeleteProduct = async (productId) => {
    await deleteProduct(productId);
    fetchProductData();
  };

  const handleCloseAddProductModal = () => {
    setShowAddProductModal(false);
    fetchProductData();
  };

  return (
    <div style={{ textAlign: 'center', marginTop: 50, marginLeft: '20%', marginRight: '20%' }}>
      <h1>Lista Produktów</h1>
      {products.length === 0 ? (
        <h4 style={{ marginBottom: 60, marginTop: 60 }}>Brak zapisanych produktów.</h4>
      ) : (
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nazwa</th>
              <th>Cena</th>
              <th>Akcje</th>
            </tr>
          </thead>
          <tbody>
            {products.map(product => (
              <tr key={product.id}>
                <td>{product.id}</td>
                <td>{product.name}</td>
                <td>{product.price} zł</td>
                <td>
                  <Button variant="danger" onClick={() => handleDeleteProduct(product.id)}>Usuń</Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
      <Button variant="primary" onClick={handleAddProductClick}>Dodaj produkt</Button>
      <AddProductModal show={showAddProductModal} onClose={handleCloseAddProductModal} />
    </div>
  );
};

export default ProductList;