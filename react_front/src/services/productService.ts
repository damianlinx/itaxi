import Product from '../models/Product';

const API_HOST = 'http://localhost:8080';

export const fetchProducts = async (): Promise<Product[]> => {
  try {
    const response = await fetch(`${API_HOST}/products`);
    if (!response.ok) {
      throw new Error('Failed to fetch products');
    }
    const data: Product[] = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching products:", error);
    return [];
  }
};

export const addProduct = async (name: string, price: number): Promise<string | null> => {
  try {
    const response = await fetch(`${API_HOST}/products`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ name, price })
    });

    if (!response.ok) {
      throw new Error('Wystąpił błąd podczas dodwania produktu.');
    }

    return response.status === 201 ? null : 'Unexpected response status';
  } catch (error) {
    console.error('Error adding product:', error);
    return error.message;
  }
};

export const deleteProduct = async (productId: number): Promise<void> => {
  try {
    const response = await fetch(`${API_HOST}/products/${productId}`, {
      method: 'DELETE'
    });
    if (!response.ok) {
      throw new Error('Failed to delete product');
    }
  } catch (error) {
    console.error('Error deleting product:', error);
    throw error;
  }
};