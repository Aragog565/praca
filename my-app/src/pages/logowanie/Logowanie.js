import './Logowanie.css';
import React, { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import { get, post } from '../../utils/Api';
import Form from 'react-bootstrap/Form';
import { useAuth } from '../../hooks/use-auth';
import { useNavigate } from 'react-router-dom';

export function Logowanie() {
    const [auth, setAuth] = useAuth();
    const navigate = useNavigate();
    const [inputs, setInputs] = useState({});
    const [errors, setErrors] = useState([]);

    const handleChange = (event) => {
        const name = event.target.name;
        const value = event.target.value;
        setInputs(values => ({...values, [name]: value}))
      }
  

    async function onSubmit(event){
        let newErrors = {};
        event.preventDefault();
        setErrors([])

        try{
            const res = await post('auth/login', inputs)
            
            const user = await get('auth/user', {
                Authorization: `Bearer ${res.data.token}`
            })
            user.data.token = res.data.token;
            setAuth(user.data)
            alert('Zostaniesz przekierowany')
          }catch(e){
            setErrors(['email lub hasło jes nie poprawne'])
          }
    }
    
    useEffect(() => {
        if (auth) navigate("/");

    }, [auth])

    return (
        <div className="container">
            <div className='login-container' >
                <form className='login' onSubmit={onSubmit}>
                    <input onChange={handleChange} name='email' type='text' placeholder='@email' required></input>
                    <input onChange={handleChange} name='password' type='password' placeholder='password' required></input>
                    <div className='wymogi'>
                            {errors?.map(e => (
                              <li>{e}</li>
                            ))}
                        </div>
                    <div >
                        <Button type='submit' variant="light">Logowanie</Button>
                        <Button href="/Rejestracja" variant="light">Rejestracja</Button>
                    </div>
                </form>
            </div>
        </div>
    )
}