import './Zamówienie.css';
import Form from 'react-bootstrap/Form';
import ListGroup from 'react-bootstrap/ListGroup';
import React, { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import { useAuth } from '../../hooks/use-auth';
import { useBasket } from '../../hooks/useBasket';
import Card from 'react-bootstrap/Card';
import { post, put } from '../../utils/Api';


export function Zamówienie() {
  const [isCheckedDostawa, setIsCheckedDostawa] = useState([true,false]);
  const [isCheckedPlatnosc, setIsCheckedPlatnosc] = useState([true,false,false,false]);useState([true,false]);
  const [auth, setAuth] = useAuth();
  const [inputs, setInputs] = useState({});
  const {basket, error, loading, removeProductBasket, removerProducts} = useBasket();
  const [errors, setErrors] = useState({});


  function checkBOXDostawa(index){
    const newChecked = [...isCheckedDostawa];
    newChecked[index] = !isCheckedDostawa[index];
    setIsCheckedDostawa(newChecked);
  } 
    
  function checkBOXPlatnosc(index){
    const newChecked = [...isCheckedPlatnosc];
    newChecked[index] = !isCheckedPlatnosc[index];
    setIsCheckedPlatnosc(newChecked);
  } 

  function handleChange(name, value){
    setInputs(values => ({...values, [name]: value}))
  }

  function getBasketPrice(dostawa=false){
    if(dostawa){
      const price = basket.reduce((total, product) => total + (product.price * product.basketCount), 0)>50 ? 0:15;
      return price
    }else{
      const price = basket.reduce((total, product) => total + (product.price * product.basketCount), 0);
      return price
    }
  }

  async function saveNewUserInfo(){
    let newErrors = {}
    try{
      const excludedProperties = ["name", "surname","phone"];
      if(auth?.address?.id!=null && Object.keys(inputs).filter(key => !excludedProperties.includes(key)).length !== 0){
        const res2 = await put(`users/${auth.id}/address/${auth.address.id}/`, inputs);
      }
      if(auth?.address?.id==null && Object.keys(inputs).filter(key => !excludedProperties.includes(key)).length !== 0){
        const res2 = await post(`users/${auth.id}/address/`, inputs);
      }
      const res = await put(`users/${auth.id}/`, inputs);
      setAuth({...res.data,
        token: auth.token
      });
    }catch(e){
      newErrors = e.response.data.message
    }
    setErrors(newErrors)
  }




  async function send(){
    let newErrors = {};
    setErrors(newErrors)
    const payment = getBasketPrice()+getBasketPrice(1);
    const products = basket.map((product)=>{
      return {productId:product.id,quantity:product.basketCount}
    })
    const form = {...inputs,payment,products};
    try{
      console.log("OK")
      await post(`order/`, form);
      
      window.location.href = '/HistoriaTransakcji';
    }catch(e){
      newErrors = e.response.data.message
      if(newErrors.bankAccount){
        newErrors.bankAccount = newErrors.bankAccount.map((error) => {
          switch(error){
            case 'must not be null':
              return 'pole nie może być puste'
            case 'must not be empty':
              return 'pole nie może być puste'
            default : 
              return error
          }
        })
      }
      const match = []
      if(e?.response?.data?.message?.bankAccount!="pole nie może być puste" && e?.response?.data?.message?.bankAccount!="zła długość: wymagane 16 znaków " && e?.response?.data?.message?.bankAccount){
        match = e?.response?.data?.message?.match(/\[(.*?)\]/);
      }
      if (match?.length > 1) {
        const numbersString = match[1]; // Pobierz dopasowany ciąg znaków z numerami
        const numbersArray = numbersString.split(',').map(Number); // Konwertuj ciąg znaków na tablicę liczb
        newErrors = numbersArray
      }
    }
    setErrors(newErrors)
  }


  return (
    <div className='wypelnij'>
      <h2>Zamówienie</h2>
      {loading ? 'Ładowanie...' : (
      <> 
        <form onSubmit={send} className='łącz'>
          <div className='podz'>
            <div className='nisko'>
              <div className='centrum'>
                <div className='dane'>
                  <h4>Twoje dane</h4>
                  <input onChange={(e)=>handleChange(e.target.name, e.target.value)} name="name" type="text" value={inputs?.name != null ? inputs.name : auth?.name} placeholder={"Imie"} required/>
                  <div className='wymogi'>
                    {errors?.name?.map(e => (
                      <li>{e}</li>
                    ))}
                  </div>
                  <input onChange={(e)=>handleChange(e.target.name, e.target.value)} name="surname" type="text" placeholder={"Nazwisko"} value={inputs?.surname != null ? inputs.surname : auth?.surname} required></input>
                  <input onChange={(e)=>handleChange(e.target.name, e.target.value)} name="city" type="text" placeholder={"Miejscowość"}  value={inputs?.city != null ? inputs.city : auth?.address?.city} required></input>
                  <input onChange={(e)=>handleChange(e.target.name, e.target.value)} name="street" type="text" placeholder={"Ulica"} value={inputs?.street != null ? inputs.street : auth?.address?.street} required></input>
                  <input onChange={(e)=>handleChange(e.target.name, e.target.value)} name="houseNumber" type="text" placeholder={"Nr dumu"} value={inputs?.houseNumber != null ? inputs.houseNumber : auth?.address?.houseNumber} required></input>
                  <input onChange={(e)=>handleChange(e.target.name, e.target.value)} name="apartmentNumber" type="text" placeholder={"Nr mieszkania"} value={inputs?.apartmentNumber != null ? inputs.apartmentNumber : auth?.address?.apartmentNumber} required></input>
                  <input onChange={(e)=>handleChange(e.target.name, e.target.value)} name="zipCode" type="text" placeholder={"Kod pocztowy"} value={inputs?.zipCode != null ? inputs.zipCode : auth?.address?.zipCode} required></input>
                  <input onChange={(e)=>handleChange(e.target.name, e.target.value)} name="phone" type="text" placeholder={"Telefon"} value={inputs?.phone != null ? inputs.phone : auth?.phone} required></input>
                  <div className='wymogi'>
                    {errors?.phone?.map(e => (
                      <li>{e}</li>
                    ))}
                  </div>
                  {
                    Object.keys(inputs).length !== 0 && auth? <Button onClick={saveNewUserInfo} variant="light">Zapisz zmiany</Button>:null
                  }
                </div>
                <div className='zamD'>
                  <div className='dostawa'>
                    <h4>Metoda dostawy</h4>
                    <Form.Check checked={isCheckedDostawa[0]} onChange={()=> checkBOXDostawa(0)} name="group1" type="radio" aria-label="radio 1" label="Kurier DPD"/>
                    <Form.Check checked={isCheckedDostawa[1]} onChange={()=> checkBOXDostawa(1)} name="group1" type="radio" aria-label="radio 2" label="Impost Paczkomat"/>
                  </div>
                  <div className='płatnosc'>
                    <h4>Metoda płatności</h4>
                    <Form.Check checked={isCheckedPlatnosc[0]} onChange={()=> checkBOXPlatnosc(1)} name="group2"type="radio" aria-label="radio 1" label="Płatność kartą"/>
                  </div>
                </div>
                <div className='dane'>
                  <h4>Wprowadź dane karty</h4>
                  Numer karty
                  <input required onChange={(e)=>handleChange(e.target.name, e.target.value)} name="bankAccount" type="text" placeholder="np:5552983744875475" pattern=".{16}" maxLength="16"></input>
                  <div className='wymogi'>
                    {  errors?.bankAccount?.map(e => (
                      <li>{e}</li>
                    ))}
                  </div>
                  Wana do 
                  <input onChange={(e)=>handleChange(e.target.name, e.target.value)} name="bank_account_valid_until" type="text" placeholder="np: 11/27" required></input>
                  Kod CVV/CVC
                  <input onChange={(e)=>handleChange(e.target.name, e.target.value)} name="bank_account_code" type="text" placeholder="np: 555" required></input>
                </div>
              </div>
              <div className='last'>
                <div className='kod'>
                  <input name="rabat" type="text" placeholder="Podaj kod rabatowy"></input>
                  <Button variant="light">Sprawdz</Button>
                </div>
              </div> 
            </div>
            <div className='podsum'>
              <h4>Wszystkie produkty</h4>
              {
                basket.map((product)=>
                  <div key = {product.id} className='productSum'>
                    <img className='msrc' src={`data:image/png;base64, ${product.photo}`}></img>
                    <div className='podMsrc'>
                      <ListGroup.Item className='mini'>Nazwa:</ListGroup.Item>
                      <ListGroup.Item className='mini'>{product.name}</ListGroup.Item>
                      <ListGroup.Item className='mini'>Ilość:</ListGroup.Item>
                      <ListGroup.Item className='mini'>{product.basketCount}</ListGroup.Item>
                      <ListGroup.Item className='mini'>Cena:</ListGroup.Item>
                      <ListGroup.Item className='mini'>{product.price}</ListGroup.Item>
                      <ListGroup.Item className='mini'>Suma:</ListGroup.Item>
                      <ListGroup.Item className='mini'>{product.basketCount*product.price}</ListGroup.Item>
                    </div>
                    <div className='wymogi'>
                      {
                        errors.length>0 && errors?.includes(product?.id) ? <li>"za mało produktu: "{product.name}" dostęmnych jest: "{product.number}</li>:null
                      }
                    </div>
                  </div>
                )
              }
            </div>
          </div>
          <div className='podzAlternative'>
            <div >
              <ListGroup className='wart' variant="flush">
                <ListGroup  variant="flush">
                  <ListGroup.Item>Suma: </ListGroup.Item>
                  <ListGroup.Item>Dostawa od: </ListGroup.Item>
                  <ListGroup.Item>Razem z dostawą: </ListGroup.Item>
                </ListGroup>
                <ListGroup  variant="flush">
                  <ListGroup.Item>{getBasketPrice()}zł</ListGroup.Item>
                  <ListGroup.Item>{getBasketPrice(true)}zł</ListGroup.Item>
                  <ListGroup.Item>{getBasketPrice()+getBasketPrice(true)}zł</ListGroup.Item>
                </ListGroup>
              </ListGroup>
            </div>
          </div>
          <div className='border1'>
            <Button type='submit' variant="light">Wyślij zamówienie</Button>
            <Button href="/Koszyk" variant="light">Edytuj zamówienie</Button>
          </div>
        </form>
      </>
      )}
    </div>
  )
}