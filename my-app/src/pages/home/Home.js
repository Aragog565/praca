import './Home.css';

import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useState, useEffect } from 'react';
import Card from 'react-bootstrap/Card';
import ListGroup from 'react-bootstrap/ListGroup';
import Carousel from 'react-bootstrap/Carousel'
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Stack from 'react-bootstrap/Stack';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Dropdown from 'react-bootstrap/Dropdown';
import { useProducts } from '../../hooks/useProducts';
import { useRecentProducts } from '../../hooks/useRecentProducts';
import { useCategories } from '../../hooks/useCategories';

export function Home() {
  const {data, error, loading} = useProducts();
  const {recentProducts, errorRecentProducts, loadingRecentProducts} = useRecentProducts();
  const {category, errorCategory, loadingCategory} = useCategories();
  const [filterProduct, setFilterProduct] = useState({});

  
  function handleChange(event){
    const name = event.target.name;
    const value = event.target.value;
    setFilterProduct(values => ({...values, [name]: value}))

  }

  return (
    <main className='main'>
      <header>
        <Stack id="search" direction="horizontal" gap={3}>
          <Form.Control className="me-auto" name="nameProduct" onChange={handleChange} placeholder="Wyszukaj ofertę po nazwie produktu..." />
            {loadingCategory ? 'Ładowanie...' : (
            <>
              <Form.Select id="category" name="categoryId" onChange={handleChange}>
                <option value={''}>Kategorie</option>
                {category.map((category) => (
                  <option key={category.id} value={category.id} >{category.name}</option>
                ))}
              </Form.Select>
            </>
            )}
          <div className="vr" />
          <Button href={`/SearchProduct?categoryId=${filterProduct.categoryId || ""}&nameProduct=${filterProduct.nameProduct || ""}`} variant="outline-danger">Wyszukaj</Button>
        </Stack>
      </header>
        <section className='test'>
          <h4>Promocje</h4>
          {
            loading ? 'Ładowanie...' : (
            <>
              <Carousel id="advertise" className='slide' >
                {
                  data.filter(p => p.promotion>0)
                  .map((productOnPromotion)=>
                    <Carousel.Item  className='item'>
                      <Carousel.Caption className='cap'> 
                        <img
                          className="sli"
                          src={`data:image/png;base64, ${productOnPromotion.photo}`}
                          alt="First slide"
                        />
                        <div>
                          <h3 className='black'>{productOnPromotion.name}</h3>
                          <p className='black'>{productOnPromotion.description}</p>
                        </div>
                      </Carousel.Caption >
                    </Carousel.Item>
                  )
                }
              </Carousel>
            </>
            )
          }
          <h1>{recentProducts.length==0 ? '':'Ostatnio przeglądane'}</h1>
          <div id='tabela'>
            {loadingRecentProducts ? 'Ładowanie...' : (
            <>
              {recentProducts.map((product) => 
                <Card style={{ width: '18rem' }} key={product.id}>
                  <Card.Img variant="top" src={`data:image/png;base64, ${product.photo}`} />
                  <Card.Body>
                    <Card.Title className='word-break-all max-3-lines'>{product.name}</Card.Title>
                    <Card.Text className='word-break-all max-3-lines'>{product.description}</Card.Text>
                  </Card.Body>
                  <ListGroup className="list-group-flush">
                    <ListGroup.Item>{product.price} zł {product.promotion ? "okazyjna cena z "+ product.promotion+"% promocją":null}</ListGroup.Item>
                  </ListGroup>
                  <Card.Body>
                    <Card.Link href={`/Produkt/${product.id}`}>Szczegóły oferty</Card.Link>
                  </Card.Body>
                </Card>
              )}
            </>
            )}
          </div>
          <h1>Inne oferty</h1>
          <div id='product'>
            {loading ? 'Ładowanie...' : (
            <>
              {data.map((product) => 
                <Card style={{ width: '18rem' }} key={product.id}>
                  <Card.Img variant="top" src={`data:image/png;base64, ${product.photo}`} />
                  <Card.Body>
                    <Card.Title className='word-break-all max-3-lines'>{product.name}</Card.Title>
                    <Card.Text className='word-break-all max-3-lines'>{product.description}</Card.Text>
                  </Card.Body>
                  <ListGroup className="list-group-flush">
                    <ListGroup.Item>{product.price} zł {product.promotion ? "okazyjna cena z "+ product.promotion+"% promocją":null}</ListGroup.Item>
                  </ListGroup>
                  <Card.Body>
                    <Card.Link href={`/Produkt/${product.id}`}>Szczegóły oferty</Card.Link>
                  </Card.Body>
                </Card>
              )}
            </>
            )}
          </div>
        </section>
    </main>
  )
}