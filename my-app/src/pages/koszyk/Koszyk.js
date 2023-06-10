import './Koszyk.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Dropdown from 'react-bootstrap/Dropdown';
import DropdownButton from 'react-bootstrap/DropdownButton';
import React, { useState, useEffect } from 'react';
import Card from 'react-bootstrap/Card';
import ListGroup from 'react-bootstrap/ListGroup';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { useBasket } from '../../hooks/useBasket';
import { useAuth } from '../../hooks/use-auth';

export function Koszyk() {
    const [isChecked, setIsChecked] = useState([]);
    const {basket, error, loading, addProductToBasket, removeProductBasket, isProductInBasket,updateProductBasket, removerProducts} = useBasket();
    const [auth, setAuth] = useAuth();

    function checkBOX(index ){
        if(index != isChecked.length - 1){
            const newChecked = [...isChecked];
            newChecked[index] = !isChecked[index];
            setIsChecked(newChecked);
        }else{
            setIsChecked(isChecked.map(() => !isChecked[isChecked.length - 1]));
        }
    } 

    function changeBasketPrice(dostawa=false){
        if(dostawa){
            const price = basket.reduce((total, product) => total + (product.price * product.basketCount), 0)>50 ? 0:15;
            return price
        }else{
            const price = basket.reduce((total, product) => total + (product.price * product.basketCount), 0);
            return price
        }
    }

    function handleNumber(id,number){
        updateProductBasket(id,number)
    }

    function removeProductFromBasket(){
        const toRemove=[]
        isChecked.forEach((item, index) => 
            {
                if(item){
                    toRemove.push(basket[index]?.id)
                }
            }
        )
        removerProducts(toRemove)
    }


    useEffect(() => {
        if (!loading && isChecked.length-1 != basket.length) {
          setIsChecked(Array(basket.length+1).fill(false));
        }
    }, [loading, basket]);

    return (
        <div className='test'>
            <main>
                <div className='szer'>
                    <Card id="CHALL" style={{ width: '18rem' }}>
                        <label>
                            <input type="checkbox"
                                checked={isChecked[isChecked.length - 1] || false}
                                onChange={() => checkBOX(isChecked.length - 1)}
                                >
                            </input>Zaznacz wszystkie
                        </label>
                        <Button onClick={removeProductFromBasket} variant="outline-danger">Usuń zaznaczone</Button>
                    </Card>
                    {loading ? 'Ładowanie...' : (
                    <>
                    {
                        basket.map((product,index)=>
                            <Card key={product.id} style={{ width: '18rem' }}>
                                <Card.Img variant="top" src={`data:image/png;base64, ${product.photo}`} />
                                <Card.Body>
                                    <Card.Title>
                                        <input type={'checkbox'} 
                                            checked={isChecked[index] || false} 
                                            onChange={()=> checkBOX(index)}>
                                        </input>
                                        {product.name}
                                    </Card.Title>
                                    <Card.Text>{product.description}s</Card.Text>
                                    <ListGroup className='usun' variant="flush">
                                        <Button onClick={()=>removeProductBasket(product.id)} variant="primary">usuń</Button>
                                        <ListGroup.Item>{product.price}</ListGroup.Item>
                                    </ListGroup>
                                </Card.Body>
                                <ListGroup className='num' variant="flush">
                                    <Button onClick={() => handleNumber(product.id,product.basketCount-1)} variant="primary">-</Button>
                                    <Form.Control onChange={(e) => handleNumber(product.id, e.target.value)} value={product.basketCount} aria-label="Example text with button addon" aria-describedby="basic-addon1"/>
                                    <Button onClick={() => handleNumber(product.id,parseFloat(product.basketCount)+1) } variant="primary">+</Button>
                                </ListGroup>
                            </Card>
                        )
                    }
                    </>
                    )}
                    <p className='space'></p>
                </div>
                <div className='platnosc'>
                    <Card style={{ width: '18rem' }}>
                        <ListGroup className='wart' variant="flush">
                            <ListGroup  variant="flush">
                                <ListGroup.Item>Suma: </ListGroup.Item>
                                <ListGroup.Item>Dostawa od: </ListGroup.Item>
                                <ListGroup.Item>Razem z dostawą: </ListGroup.Item>
                            </ListGroup>
                            <ListGroup  variant="flush">
                                <ListGroup.Item>{changeBasketPrice()}zł</ListGroup.Item>
                                <ListGroup.Item>{changeBasketPrice(1)}zł</ListGroup.Item>
                                <ListGroup.Item>{changeBasketPrice()+changeBasketPrice(1)}zł</ListGroup.Item>
                            </ListGroup>
                        </ListGroup>
                        <ListGroup  variant="flush">
                            <ListGroup.Item className='srod'><Button href={auth && basket.length>0 ? '/Zamówienie' : '/Logowanie'} variant="light">{basket.length>0?"Złóż zamówienie":"koszyk jest pusty"}</Button></ListGroup.Item>
                        </ListGroup>
                        <Card.Body className='srod'>
                            <Card.Link href={`/SearchProduct`}>Kontynuuj zakupy</Card.Link>
                        </Card.Body>
                    </Card>
                </div>
            </main>
        </div>
    )
}