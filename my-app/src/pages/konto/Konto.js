import './Konto.css';
import React, { useState, useEffect } from 'react';
import * as ReactDOM from 'react-dom';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import ListGroup from 'react-bootstrap/ListGroup';
import { useAuth } from '../../hooks/use-auth';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';
import { put } from '../../utils/Api';

export function Konto() {
    const [auth, setAuth] = useAuth();
    const [change, setChange] = useState([true,false,false,false,false,false]);
    const [imageSrc, setImageSrc] = useState('');
    const [inputs, setInputs] = useState({});
    const [isChecked, setIsChecked] = useState([]);


    function changeWindow(index){
        const newchange=[...change.map((index2) => !change[index2]==false)]
        newchange[index]=!change[index]
        setChange(newchange)
    }

    function handleImageUpload(event) {
        const file = event.target.files[0];
        const reader = new FileReader();
        reader.onloadend = () => {
          setImageSrc(reader.result);
        };
        
        reader.readAsDataURL(file);
        setInputs(values => ({...values, [event.target.name]: file}))
    }

    function handleChange(name,value){
        setInputs(values => ({...values, [name]: value}))
    }

    function checkBOX(index,name){
        const newChecked = [...isChecked];
        newChecked[index] = !isChecked[index];
        setInputs({...inputs,[name]: newChecked[index] })
        setIsChecked(newChecked);
      } 

    async function sendUser(){
        let res ;
        if(inputs.file){
            try{
                const form = new FormData();
                for (const [key,value] of Object.entries(inputs)){
                    form.append(key, value);
                }
                res = await put(`users/${auth.id}/photo`, form);

            }catch(e){

            }
        }
        if(inputs && (Object.keys(inputs).length > 1 || !('file' in inputs))){
            try{
                res = await put(`users/${auth.id}`, inputs);
            }catch(e){

            }
        }
        changeWindow(0)
        setAuth({
            ...res.data,
            token: auth.token
        })        
    }

    async function sendAddress(){
        try{
            const res = await put(`users/${auth.id}/address/${auth.address.id}`, inputs);

        }catch(e){
        }
    }

    useEffect(() => {
        setIsChecked([auth.specialOffers])
    },[auth])
    

    return (
        <div className='cal'>
            <form className='konto'>
                <Card id="profil" style={{ width: '18rem' }}>
                    <Card.Img variant="top" src={auth.photo ? `data:image/png;base64, ${auth.photo}` : "/assets/user.jpg"} />
                    <ListGroup className="list-group-flush">
                        <ListGroup.Item>Imię: {auth.name}</ListGroup.Item>
                        <ListGroup.Item>Nazwisko: {auth.surname}</ListGroup.Item>
                        <ListGroup.Item>Email: {auth.email}</ListGroup.Item>
                        <ListGroup.Item></ListGroup.Item>
                    </ListGroup>
                </Card>
                <Card id="ustawienia">
                    {
                        change[0] ?
                        <Card.Body>
                            <Card.Header as="h5">Ustawienia konta</Card.Header>
                            <ListGroup variant="flush">
                                <ListGroup.Item onClick={()=>changeWindow(1)} id="ustawienia">Informacje o Twoim koncie<Card.Link id="ust">ZMIEŃ</Card.Link></ListGroup.Item>
                                <ListGroup.Item onClick={()=>changeWindow(2)} id="ustawienia">Zarządzaj swoim adresem<Card.Link id="ust" >ZMIEŃ</Card.Link></ListGroup.Item>
                                <ListGroup.Item onClick={()=>changeWindow(3)} id="ustawienia">Zgody na powiadomienia<Card.Link id="ust" >ZMIEŃ</Card.Link></ListGroup.Item>
                                {/* <ListGroup.Item id="ustawienia">Zmień adres e-mail<Card.Link id="ust" >ZMIEŃ</Card.Link></ListGroup.Item>
                                <ListGroup.Item onClick={} id="ustawienia">Zmień hasło<Card.Link id="ust">ZMIEŃ</Card.Link></ListGroup.Item> */}
                            </ListGroup>
                        </Card.Body>
                        :
                        change[1] ?
                        <Card.Body>
                            <Card.Header as="h5">Informacje o Twoim koncie</Card.Header>
                            <ListGroup variant="flush">
                                <ListGroup.Item id="myNewPhoto">
                                    <h6>Zmień swoje zdjęcie</h6>
                                    <input name="file" accept="image/png, image/jpeg" type="file" onChange={handleImageUpload}/>
                                </ListGroup.Item>
                                <Card.Img id='src' variant="top" src={imageSrc} />
                                <ListGroup.Item >
                                    
                                    <InputGroup id="name" className="mb-3">
                                        <InputGroup.Text className="fitInput" id="basic-addon3">
                                        {auth?.name}
                                        </InputGroup.Text>
                                        <Form.Control  name="name" onChange={(e)=>handleChange(e.target.name,e.target.value)}  aria-describedby="basic-addon3" placeholder={"Podaj nowe imię"}/>
                                    </InputGroup>
                                    <InputGroup id="surname" className="mb-3">
                                        <InputGroup.Text className="fitInput" id="basic-addon3">
                                        {auth?.surname}
                                        </InputGroup.Text>
                                        <Form.Control name="surname" onChange={(e)=>handleChange(e.target.name,e.target.value)}  aria-describedby="basic-addon3" placeholder={"Podaj nowe nazwisko"}/>
                                    </InputGroup>
                                    <InputGroup id="phone" className="mb-3">
                                        <InputGroup.Text className="fitInput" id="basic-addon3">
                                        {auth?.phone}
                                        </InputGroup.Text>
                                        <Form.Control name="phone" onChange={(e)=>handleChange(e.target.name,e.target.value)}  aria-describedby="basic-addon3" placeholder={"Podaj nowy numer telefonu"}/>
                                    </InputGroup>
                                    <InputGroup id="email" className="mb-3">
                                        <InputGroup.Text className="fitInput" id="basic-addon3">
                                        {auth?.email}
                                        </InputGroup.Text>
                                        <Form.Control name="email" onChange={(e)=>handleChange(e.target.name,e.target.value)}  aria-describedby="basic-addon3" placeholder={"Podaj nowy e-mail"}/>
                                    </InputGroup>
                                    <InputGroup id="password" className="mb-3">
                                        <Form.Control autoComplete="off" name="password" type="password" onChange={(e)=>handleChange(e.target.name,e.target.value)} id="basic-url" aria-describedby="basic-addon3" placeholder={"Podaj nowy hasło"}/>
                                    </InputGroup>
                                </ListGroup.Item>
                            </ListGroup>
                            <ListGroup id="next" variant="flush">
                                <Button id="confirm" className="btn btn-light congirm" onClick={sendUser} variant="light">Zatwierdz zmiany</Button>
                                <Button id="canceled" className="btn btn-light congirm" onClick={()=>changeWindow(0)} variant="light">Anuluj</Button>
                            </ListGroup>
                        </Card.Body>
                        :
                        change[2] ?
                        <Card.Body>
                            <Card.Header as="h5">Zmień swoje dane</Card.Header>
                            <ListGroup variant="flush">
                                <ListGroup.Item  id="adress">
                                    <InputGroup id="city" className="mb-3">
                                        <InputGroup.Text className="fitInput" id="basic-addon3">
                                        {auth.address?.city}
                                        </InputGroup.Text>
                                        <Form.Control name="city" onChange={(e)=>handleChange(e.target.name,e.target.value)} id="basic-url" aria-describedby="basic-addon3" placeholder={"Miasto"}/>
                                    </InputGroup>
                                    <InputGroup id="street" className="mb-3">
                                        <InputGroup.Text className="fitInput" id="basic-addon3">
                                        {auth.address?.street}
                                        </InputGroup.Text>
                                        <Form.Control name="street" onChange={(e)=>handleChange(e.target.name,e.target.value)} id="basic-url" aria-describedby="basic-addon3" placeholder={"Ulica"}/>
                                    </InputGroup>
                                    <InputGroup id="zipCode" className="mb-3">
                                        <InputGroup.Text className="fitInput" id="basic-addon3">
                                        {auth.address?.zipCode}
                                        </InputGroup.Text>
                                        <Form.Control name="zipCode" onChange={(e)=>handleChange(e.target.name,e.target.value)} id="basic-url" aria-describedby="basic-addon3" placeholder={"Kod pocztowy"}/>
                                    </InputGroup>
                                    <InputGroup id="houseNumber" className="mb-3">
                                        <InputGroup.Text className="fitInput" id="basic-addon3">
                                        {auth.address?.houseNumber}
                                        </InputGroup.Text>
                                        <Form.Control name="houseNumber" onChange={(e)=>handleChange(e.target.name,e.target.value)} id="basic-url" aria-describedby="basic-addon3" placeholder={"numer domu"}/>
                                    </InputGroup>
                                    <InputGroup id="apartmentNumber" className="mb-3">
                                    <InputGroup.Text className="fitInput" id="basic-addon3">
                                        {auth.address?.apartmentNumber}
                                        </InputGroup.Text>
                                        <Form.Control name="apartmentNumber" onChange={(e)=>handleChange(e.target.name,e.target.value)} id="basic-url" aria-describedby="basic-addon3" placeholder={"numer mieszkania"}/>
                                    </InputGroup>
                                </ListGroup.Item>
                            </ListGroup>
                            <ListGroup id="next" variant="flush">
                                <Button id="confirm" className="btn btn-light congirm" onClick={sendAddress} variant="light">Zatwierdz zmiany</Button>
                                <Button id="canceled" className="btn btn-light congirm" onClick={()=>changeWindow(0)} variant="light">Anuluj</Button>
                            </ListGroup>
                        </Card.Body>
                        :
                        change[3] ?
                        <Card.Body>
                            <Card.Header as="h5">Zgody na powiadomienia</Card.Header>
                            <ListGroup variant="flush">
                                <ListGroup.Item id="ustawienia">
                                    <Form.Group className="mb-3" controlId="formBasicCheckbox">
                                        Chcę otrzymywać kody zniżkowe, oferty specjalne lub inne treści marketingowe.
                                        <Form.Check type="checkbox" checked={isChecked[0]} onChange={()=> checkBOX(0,'specialOffers')} label="na aplikacje"/>
                                    </Form.Group>
                                </ListGroup.Item>
                            </ListGroup>
                            <ListGroup id="next" variant="flush">
                                <Button id="confirm" className="btn btn-light congirm" onClick={sendUser} variant="light">Zatwierdz zmiany</Button>
                                <Button id="canceled" className="btn btn-light congirm" onClick={()=>changeWindow(0)} variant="light">Anuluj</Button>
                            </ListGroup>
                        </Card.Body>
                        :
                        null
                    }
                </Card>
            </form>
        </div>
    )
}