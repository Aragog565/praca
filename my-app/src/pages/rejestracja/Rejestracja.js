import './Rejestracja.css';
import React, { useState, useEffect } from 'react';

import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { post } from '../../utils/Api';
import Alert from 'react-bootstrap/Alert';


export function Rejestracja() {
  const [isChecked, setIsChecked] = useState([false,false]);
  const [isAll, setIsAll] = useState(false);
  const [inputs, setInputs] = useState({});
  const [show, setShow] = useState(false);
  const [errors, setErrors] = useState({
    confirmedPassword: ["min. 7 znaków",
      "1 wielka litera",
      "1 znak specjalny",
      "1 cyfra",
    ]
  });
  
    
  function AllCheckbox(){
    setIsChecked([!isAll, !isAll]);
    setIsAll(!isAll)
  }

  function checkBOX(index,name){
    const newChecked = [...isChecked];
    newChecked[index] = !isChecked[index];
    setInputs({...inputs,[name]: newChecked[index] })
    setIsChecked(newChecked);
  } 

  async function onSubmit(event){
    let newErrors = {}
    event.preventDefault();
    setErrors('')
    if(isChecked[0]===false){
      newErrors.regulamin = ['Należy zaznaczyć pole.'];
      return setErrors(newErrors)
    }
    if(inputs.password != inputs.confirmedPassword){
      newErrors.confirmedPassword=["hasła różnią się"];
      setErrors(newErrors)
      return;
    }
    try{
      const res = await post('auth/register', inputs)
      setShow(true)
    }catch(e){
      newErrors = e.response.data.message
      if(newErrors.password){
        newErrors.password = newErrors.password.map((error) => {
          switch(error){
            case 'Password must be at least 7 characters in length.':
              return 'Hasło musi mieć co najmniej 7 znaków.'
            case 'Password must contain at least 1 uppercase characters.':
              return 'Hasło musi zawierać co najmniej 1 wielką literę.'
            case 'Password must contain at least 1 special characters.':
              return 'Hasło musi zawierać co najmniej 1 znak specjalny.'
          }
        })
      }
      setErrors(newErrors)
    }
  }

  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputs(values => ({...values, [name]: value}))
  }

  return (
    <div className="cal">
      <Alert id="alertR" show={show} variant="success">
        <Alert.Heading>Rejestracja</Alert.Heading>
        <p>
          Rejestracja została zakończona.
        </p>
        <hr />
        <div className="d-flex justify-content-end ">
          <Button href="/" onClick={() => setShow(false)} variant="outline-success">
            Zamknij
          </Button>
        </div>
      </Alert>
      <h1 className='rej'>Zarejestruj</h1>
      <form className="log" onSubmit={onSubmit}>
        <div className='Dlogowanie'>
          <h4>Dane logowania</h4>
          <div className='rejestracja'>
            email
              <input onChange={handleChange}  name="email" type="text" placeholder="@email" required></input>
              <div className='wymogi'>
                  {errors?.email?.map(e => (
                    <li key='e-mail'>{e}</li>
                  ))}
              </div>
              hasło
              <input onChange={handleChange} autoComplete="off"  name="password" type="password" placeholder="password" required></input>
              <div className='wymogi'>
                  {errors?.password?.map(e => (
                    <li key='password'>{e}</li>
                  ))}
              </div>
              potwierdź hasło
              <input onChange={handleChange} autoComplete="off" name="confirmedPassword" type="password" placeholder="confirm password" required></input>                        
              <div className='wymogi'>
                  {errors?.confirmedPassword?.map((e,i) => (
                    <li key={i}>{e}</li>
                  ))}
              </div>
              imię
              <input onChange={handleChange}  name="name" type="text" placeholder="name" required></input>
              <div className='wymogi'>
                  {errors?.name?.map(e => (
                    <li key='name'>{e}</li>
                  ))}
              </div>
              nazwisko
              <input onChange={handleChange}  name="surname" type="text" placeholder="surname" required></input>
              <div className='wymogi'>
                  {errors?.message?.map(e => (
                    <li key='surname'>{e}</li>
                  ))}
              </div>
          </div>
        </div>
        <div className='oswiadczenia'>
          <h4>Zgoda i oświadczenia</h4>
          <Form.Group className="mb-3" controlId="formBasicCheckbox">
            <Form.Check id='all' type="checkbox" label="Zatwierdz wszystko" onClick={()=>AllCheckbox()}/>
            <Form.Check id='terms' name='terms_and_conditions' type="checkbox" label="Oświadczam, że znam i akceptuję postanowienia Regulaminu." 
            checked={isChecked[0]} onChange={()=> checkBOX(0,'termsAndConditions')}/>
            <div className='wymogi'>
              {errors?.regulamin?.map(e => (
                <li key="terms_and_conditions">{e}</li>
              ))}
            </div>
            <Form.Check id='offers' name='special_offers' type="checkbox" label="Chcę otrzymywać kody zniżkowe, oferty specjalne lub inne treści marketingowe." 
            checked={isChecked[1]} onChange={()=> checkBOX(1,'specialOffers')}/>
          </Form.Group>
        </div>
        <Button type='submit' variant="light">Zarejestruj</Button>
      </form>
    </div>
  )
}