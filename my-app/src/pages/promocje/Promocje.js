import './Promocje.css';
import React, { useState, useEffect } from 'react';
import * as ReactDOM from 'react-dom';
import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Stack from 'react-bootstrap/Stack';
import { useProducts } from '../../hooks/useProducts';
import { useCategories } from '../../hooks/useCategories';

export function Promocje() {
    const {data, error, loading} = useProducts();
    const {category, errorCategory, loadingCategory} = useCategories();
    const [filterProduct, setFilterProduct] = useState({});
    const [filterProductWaiting, setFilterProductWaiting] = useState({});
    
    function handleChange(event){
        const name = event.target.name;
        const value = event.target.value;
        setFilterProductWaiting(values => ({...values, [name]: value}))
    }

    function startFilter(){
        setFilterProduct(filterProductWaiting)
    }

    return (
        <div className='cal'>
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
                    <Button onClick={startFilter} variant="outline-danger">Wyszukaj</Button>
                </Stack>
            </header>
            <div id='promotion'>
                {
                    data.filter(p => !filterProduct.categoryId || p.category.id == filterProduct.categoryId)
                        .filter(p => !filterProduct.nameProduct || p.name.includes(filterProduct.nameProduct))
                        .filter(p => p.promotion>0)
                        .map((product)=>
                            <Button href={`/Produkt/${product.id}`} variant="light">
                                <Card.Img id="srcD" variant="top" src={`data:image/png;base64, ${product.photo}`} />
                                <Card.Body>
                                    <Card.Title id="title" className='word-break-all max-3-lines'>{product.name}</Card.Title>
                                    <Card.Text id="rabat">
                                        -{product.promotion}%
                                    </Card.Text>
                                </Card.Body>
                            </Button>
                        )
                }
            </div>
        </div>
    )
}