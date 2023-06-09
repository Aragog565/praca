import './SearchProduct.css';
import React from 'react';
import Form from 'react-bootstrap/Form';
import Stack from 'react-bootstrap/Stack';
import { useCategories } from '../../hooks/useCategories';
import { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import Card from 'react-bootstrap/Card';
import ListGroup from 'react-bootstrap/ListGroup';
import { useProducts } from '../../hooks/useProducts';
import { post } from '../../utils/Api';
import Alert from 'react-bootstrap/Alert';
import Button from 'react-bootstrap/Button';
import { useNavigate, useParams, createSearchParams } from 'react-router-dom';
import { useQuery } from '../../hooks/useQuery';



export function SearchProduct() {
    let query = useQuery();
    const navigate = useNavigate();
    const [filterProduct, setFilterProduct] = useState({
        categoryId: query.get('categoryId') || '',
        nameProduct: query.get('nameProduct') || ''
    });
    const {category, errorCategory, loadingCategory} = useCategories();
    const {data, error, loading, updateData} = useProducts();
    const [show, setShow] = useState(false);

    async function handleRatingClick(event,id){
        try{
            const res = await post(`products/${id}/rate`, {value: event});
            updateData(res.data.product)
        }catch(e){
            setShow(true);
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }
    }
    function handleChange(event){
        const name = event.target.name;
        const value = event.target.value;
        const params = {...filterProduct, [name]: value}
        navigate({
            pathname: '',
            search: `?${createSearchParams(params)}`
        })
        setFilterProduct(values => params)
    }

    
    function getLabel(){
        const searchCategory = category.find((category) => (
            filterProduct.categoryId?category.id==filterProduct.categoryId:category.id==filterProduct.categoryId
        ))
        if(!searchCategory && filterProduct.categoryId>0){
            return 'Nie ma takiej kategori'
        }
        let myLabel= filterProduct.nameProduct ? 'Szukasz ':'Czego szukasz ';

        if(filterProduct.nameProduct){
            myLabel=myLabel+filterProduct.nameProduct
            if(filterProduct.categoryId){
                myLabel=myLabel+' z kategorii '+searchCategory.name
                return myLabel
            }
        }
        if(filterProduct.categoryId){
            myLabel=myLabel+' w kategorii '+searchCategory.name
            return myLabel
        }
        return myLabel
    }


    return (
        <div className="container">
            <header>
                <Stack id="search" direction="horizontal" gap={3}>
                    <Form.Control value={filterProduct.nameProduct} className="me-auto" name="nameProduct" onChange={handleChange} placeholder="Wyszukaj ofertę po nazwie produktu..." />
                        {loadingCategory ? 'Ładowanie...' : (
                        <>
                            <Form.Select value={filterProduct.categoryId} id="category" name="categoryId" onChange={handleChange}>
                                <option value={''}>Kategorie</option>
                                {category.map((category) => (
                                    <option key={category.id} value={category.id}>{category.name}</option>
                                ))}
                            </Form.Select>
                        </>
                    )}
                    <div className="vr" />
                </Stack>
            </header>
            <Alert id="alertR" show={show} variant="success">
                <Alert.Heading>Logowanie</Alert.Heading>
                <p>
                    Zaloguj się.
                </p>
                <hr />
                <div className="d-flex justify-content-end ">
                    <Button onClick={() => setShow(false)} variant="outline-success">
                        Zamknij
                    </Button>
                    <Button href="/logowanie" onClick={() => setShow(false)} variant="outline-success">
                        Do logowania
                    </Button>
                </div>
            </Alert>
            <h1>{ getLabel()}</h1>
            <div id='searchProduct'>
                <Card  >
                    {loading ? 'Ładowanie...' : (
                    <>
                        {data.filter(p => !filterProduct.categoryId || p.category.id == filterProduct.categoryId)
                            .filter(p => !filterProduct.nameProduct || p.name.includes(filterProduct.nameProduct))
                            .map((product) => 
                                <div key = {product.id} id="cardSearch">
                                    <Card.Img id="imgSearch" variant="top" src={`data:image/png;base64, ${product.photo}`} />
                                    <Card.Body>
                                        <Card.Title>{product.name}</Card.Title>
                                        <div className="rating">
                                            <span>{product.totalRates} ocen peoduktu</span>
                                            <div className="stars" >
                                                {Array(5).fill().map((v, i) => (
                                                    <span key={i+"star"} 
                                                        name="value"
                                                        className={i < Math.round(product.avgRate) ? 'highlighted-star' : ''} 
                                                        onClick={() => handleRatingClick(i+1,product.id)} >&#9733;
                                                    </span>
                                                ))}{(product.avgRate.toFixed(2))}
                                            </div>
                                        </div>
                                        <Card.Text className='word-break-all max-3-lines'>{product.description}</Card.Text>
                                    </Card.Body>
                                    <Card.Body>
                                        <ListGroup.Item>{product.price} zł</ListGroup.Item>
                                        <ListGroup.Item>{product.number} dostępne</ListGroup.Item>
                                        <Card.Link href={`/Produkt/${product.id}`}>Szczegóły oferty</Card.Link>
                                    </Card.Body>
                                </div>
                            )
                        }
                    </>
                    )}
                </Card>
            </div>
        </div>
    )
}