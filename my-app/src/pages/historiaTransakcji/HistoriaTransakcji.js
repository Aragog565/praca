import './HistoriaTransakcji.css';
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

export function HistoriaTransakcji() {
    const [auth, setAuth] = useAuth();
    const {order, errorOrder, loadingOrder, updateOrder, removeOrders, updateOrderArray} = useOrde(auth.id);
    const [isChecked, setIsChecked] = useState([]);
    const [filterOrder, setFilterOrder] = useState({});


    function toLocalTime(orderTime){
        const createdAt = new Date(orderTime);
        const formattedDate = createdAt.toLocaleDateString();
        const formattedTime = createdAt.toLocaleTimeString();

        return formattedTime+" - "+formattedDate
    }

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

    function handleChange(name, value){
        setFilterOrder({})
        if(value==""){
            const currentTime = new Date();
            value = currentTime.toISOString();
        }
        setFilterOrder(values => ({...values, [name]: value}))
    }

    
    

    async function removeOrder(){
        const toRemove =[];
        isChecked.forEach((item,index)=>
            {
                if(item && order[index]?.id){
                    toRemove.push(order[index]?.id)
                }
            }
        )
        const res = await remove(`order`,{ids:toRemove.join(',')});
        removeOrders(toRemove)
    }

    async function cancelOrder(name,value){
        const toUpdate =[];
        isChecked.forEach((item,index)=>
            {
                if(item && order[index]?.id){
                    toUpdate.push(order[index])
                }
            }
        )
        const ids={ids:toUpdate.map((order)=>order.id).join(',')}

        
        const res = await put(`order?ids=${ids.ids}`,{cancelled:true});
        toUpdate.forEach((order)=>order.cancelled=true)
        updateOrderArray(toUpdate)
    }

    useEffect(() => {
        if (!loadingOrder) {
          setIsChecked(Array(order.length+1).fill(false));
        }
    }, [loadingOrder, order]);
    

    return (
        <div className='orders'>
            <header>
                <Card >
                    <Stack id="Wcard"direction="horizontal" gap={3}>
                        <ListGroup id="kat-sort" variant="flush">
                            <DropdownButton variant="primary" drop='end' id="dropdown-basic-button" title="Sortowanie">
                                <Dropdown.Item name="dateOrder" onClick={(e)=>handleChange(e.target.name, -1)} >Ostatnio dodane</Dropdown.Item>
                                <Dropdown.Item name="dateOrder" onClick={(e)=>handleChange(e.target.name, 1)} >Najstarsze</Dropdown.Item>
                            </DropdownButton>
                            <Button name="dateOfPayment" onClick={(e)=>handleChange(e.target.name, e.target.value)} variant="danger">Nieopłacone</Button>
                            <Button nema="completionDate" onClick={(e)=>handleChange(e.target.name, e.target.value)} variant="warning">Do odbioru</Button>
                            <Button name="cancelled" onClick={(e)=>handleChange(e.target.name, e.target.value)} variant="dark">Anulowane</Button>
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
                <Button onClick={removeOrder} variant="outline-danger">Usuń zaznaczone</Button>
                <Button name="cancelled" value={true} onClick={(e)=>cancelOrder(e.target.name, e.target.value)} variant="outline-danger">Anuluj</Button>
            </Card>
            <h4>Zamówienia</h4>
            {loadingOrder ? 'Ładowanie...' : (
            <>
                {
                    order.length>0 ?
                    order.filter(p=> !filterOrder.dateOfPayment || p.dateOfPayment>filterOrder.dateOfPayment)
                    .filter(p=> !filterOrder.completionDate || p.completionDate>filterOrder.completionDate)
                    .filter(p=> !filterOrder.cancelled || p.cancelled==true)
                    .sort((a,b)=>{
                        return filterOrder.dateOrder * (new Date(a.createdAt) - new Date(b.createdAt))
                    })
                    .map((order,index)=>
                    <Card key={order.id} id = "transakcja" style={{ width: '100%'}}>
                        <label id="myCbox">
                            <input type="checkbox" onChange={()=> checkBOX(index)} checked={isChecked[index] || false}/>
                            {toLocalTime(order.createdAt)}
                        </label>
                        {order?.orderProduct?.map(element => {
                           return <ListGroup id="g1" variant="flush" key={element.id}>
                                <Card.Img variant="top" src={`data:image/png;base64, ${element.product.photo}`} />
                                <ListGroup id="g4" variant="flush">
                                    <ListGroup.Item id="g3">Nazwa: {element.product.name}</ListGroup.Item>
                                    <ListGroup.Item id="g3">Ilość: {element.quantity}</ListGroup.Item>
                                    <ListGroup.Item>Cena: {element.netPrice}</ListGroup.Item>
                                </ListGroup>
                            </ListGroup>
                        })}
                         <ListGroup id="g2" variant="flush">
                            <ListGroup.Item id="g3">Razem </ListGroup.Item>
                            <ListGroup.Item id="g3">{order.payment} </ListGroup.Item>
                        </ListGroup>
                    </Card>
                    )
                    :
                    <Card key={order.id} id = "transakcja" style={{ width: '100%'}}>
                        <h5>Nie posiadasz żadnych zamówień</h5>
                    </Card>
                }
            </>
            )}
        </div>
    )
}