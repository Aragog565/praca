import './ForAdmin.css';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import { useState } from 'react';
import { post } from '../../utils/Api';

export function ForAdmin() {
    const [inputs, setInputs] = useState({});

    async function createCategory(){
        console.log(inputs)
        try{
            const res = await post(`categories`, inputs);
            console.log(res)
            window.location.href = '/';
            // updateData(res.data.product)
            
        }catch(e){
            console.log(e)
        }
    }

    function handleChange(name,value){
        setInputs(values => ({...values, [name]: value}))
    }
  
    return (
        <div>
            <Card style={{ width: '50%' }}>
                tworzenie kategori
                <Form.Control id="alignment" onChange={(e)=>handleChange(e.target.name,e.target.value)}  name="name" type= "text" className="me-auto" placeholder={"Nazwa kategori"} required/>
                <Button id="congirm" onClick={createCategory} type='submit' variant="light">Zatwierdz</Button>
            </Card>
        </div>
    )
  }