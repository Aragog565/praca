import './MyProduct.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Dropdown from 'react-bootstrap/Dropdown';
import DropdownButton from 'react-bootstrap/DropdownButton';
import React, { useState, useEffect } from 'react';
import Card from 'react-bootstrap/Card';
import ListGroup from 'react-bootstrap/ListGroup';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Stack from 'react-bootstrap/Stack';
import { useAuth } from '../../hooks/use-auth';
import { useOrde } from '../../hooks/useOrde';
import { put, remove } from '../../utils/Api';
import { useProducts } from '../../hooks/useProducts';

export function MyProduct() {
    const [auth, setAuth] = useAuth();
    const [isChecked, setIsChecked] = useState([]);
    const [filterOrder, setFilterOrder] = useState({});
    const {data, error, loading, removeItems, updateData} = useProducts();
    const [inputs, setInputs] = useState({});

    function toLocalTime(orderTime){
        const createdAt = new Date(orderTime);
        const formattedDate = createdAt.toLocaleDateString();
        const formattedTime = createdAt.toLocaleTimeString();

        return formattedTime+" - "+formattedDate
    }

    console.log(data)
    function checkBOX(index ){
        if(index != isChecked.length - 1){
            const newChecked = [...isChecked];
            newChecked[index] = !isChecked[index];
            newChecked[isChecked.length - 1]=false;
            setIsChecked(newChecked);
        }else{
            setIsChecked(isChecked.map(() => !isChecked[isChecked.length - 1]));
        }
    } 

    function handleFilter(name, value){
        setFilterOrder({})
        if(value==""){

            const currentTime = new Date();
            value = currentTime.toISOString();
        }
        setFilterOrder(values => ({...values, [name]: value}))
    }

    function handleChange(name, value){

        setInputs(values => ({...values, [name]: value}))
    }

    async function removeProduct(){
        const toRemove =[];
        isChecked.forEach((item,index)=>
            {
                if(item && data[index]?.id){
                    toRemove.push(data[index]?.id)
                }
            }
        )
        const res = await remove(`products`,{ids:toRemove.join(',')});
        removeItems(toRemove)
    }

    async function editProduct(event,id){
        event.preventDefault();
        console.log({inputs})
        const res = await put(`products/${id}`,inputs);
        console.log({res})
        // updateData(res.date)

    }

    useEffect(() => {
        if (!loading) {
          setIsChecked(Array(data.filter(p=>  p.user.id==auth.id).length+1).fill(false));
        }
    }, [loading, data]);
    

    return (
        <div className='orders'>
            <header>
                <Card >
                    <Stack id="Wcard"direction="horizontal" gap={3}>
                        <ListGroup id="kat-sort" variant="flush">
                            <DropdownButton variant="primary" drop='end' id="dropdown-basic-button" title="Sortowanie">
                                <Dropdown.Item name="dateOrder" onClick={(e)=>handleFilter(e.target.name, -1)} >Ostatnio dodane</Dropdown.Item>
                                <Dropdown.Item name="dateOrder" onClick={(e)=>handleFilter(e.target.name, 1)} >Najstarsze</Dropdown.Item>
                            </DropdownButton>

                        </ListGroup>
                        <div className="vr" />
                    </Stack>
                </Card>
            </header>
            <Card id="CHALL" style={{ width: '18rem' }}>
                <label>
                    <input type="checkbox"
                        checked={isChecked[isChecked.length - 1] || false}
                        onChange={() => checkBOX(isChecked.length - 1)}>
                    </input>Zaznacz wszystkie
                </label>
                <Button onClick={removeProduct} variant="outline-danger">Usuń zaznaczone</Button>
            </Card>
            <h4>Twoje produkty</h4>
            {loading ? 'Ładowanie...' : (
            <>
                {
                    data.filter(p=>  p.user.id==auth.id)
                    .sort((a,b)=>{
                        return filterOrder.dateOrder * (new Date(a.createdAt) - new Date(b.createdAt))
                    })
                    .map((product,index)=>
                        <Card style={{ width: '100%'}}>
                            <label id="myCbox">
                                <input type="checkbox" onChange={()=> checkBOX(index)} checked={isChecked[index] || false}/>
                                {toLocalTime(product.createdAt)}
                            </label>
                            <ListGroup id="myProduct" variant="flush" key={product.id}>
                                <Card.Img variant="top" src={`data:image/png;base64, ${product.photo}`} />
                                <ListGroup id="g4" variant="flush">
                                    <ListGroup.Item id="g3">Nazwa: {product.name}</ListGroup.Item>
                                    <ListGroup.Item id="g3">Ilość: {product.number}</ListGroup.Item>
                                    <ListGroup.Item>Cena: {product.price}</ListGroup.Item>
                                    <ListGroup.Item>Promocja: {product.promotion>0 ?  product.promotion : "brak"}</ListGroup.Item>
                                </ListGroup>
                            </ListGroup>
                            <ListGroup id="promotion" variant="flush" key={`${product.id}_${index}`}>
                                <ListGroup.Item id="g3">{product.promotion>0 ?  "Wpisz zero aby usunąć promocje":"Dodaj promocje do produktu"}</ListGroup.Item>
                                <Form.Control id="alignment" onChange={(e)=>handleChange(e.target.name,e.target.value)} name="promotion" type= "text" className="me-auto" placeholder={"podaj liczbe np:10 "} required/>
                                <Button onClick={(e)=>editProduct(e,product.id)} variant="light">Zmień</Button>
                            </ListGroup>
                        </Card>
                    )
                }
            </>
            )}
        </div>
    )
}