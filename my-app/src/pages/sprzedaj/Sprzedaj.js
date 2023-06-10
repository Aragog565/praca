import './Sprzedaj.css';
import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Heart from "react-heart"
import ListGroup from 'react-bootstrap/ListGroup';
import Form from 'react-bootstrap/Form';
import { post } from '../../utils/Api';
import FloatingLabel from 'react-bootstrap/FloatingLabel';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Dropdown from 'react-bootstrap/Dropdown';
import {useCategories } from '../../hooks/useCategories';

/*npm install react-heart*/ 



export function Sprzedaj() {
    const {category, errorCategory, loadingCategory} = useCategories();
    const [rating, setRating] = useState(4);
    const [totalRatings, setTotalRatings] = useState(1);
    const [favorite, setFavorite] = useState(false);
    const [ilość, setIlość] = useState(0);
    const [imageSrc, setImageSrc] = useState('');
    const [inputs, setInputs] = useState({
        vat:0.23,  
        parameters: [{
            name: "",
            value: ""
        }]
    });



    async function onSubmit(event){
        event.preventDefault();
        const form = new FormData();
        for (const [key,value] of Object.entries(inputs)){
            if(key === 'parameters'){
                for(let i = 0; i < value.length; i++){
                    form.append(`${key}[${i}].name`, value[i].name);
                    form.append(`${key}[${i}].value`, value[i].value);
                }
                continue;
            }
            form.append(key, value);
        }
        const res = await post(`products`,form);
        window.location.href = '/MyProduct';
    }

    const handleChange = (event) => {
        const name = event.target.name;
        const value = event.target.value;
        setInputs(values => ({...values, [name]: value}))
    }

    function handleRatingClick(wart){
        const newTotalRating = totalRatings+1
        setTotalRatings(newTotalRating)
        const newRating = (rating*totalRatings+wart)/newTotalRating
        setRating(newRating)
    }

    function handleFavoriteClick() {
        setFavorite(!favorite);
    }
    
    function dodajParametr(){
        const parameters = inputs.parameters;
        parameters.push({name: "", value: ""});
        setInputs(values => ({...values, parameters}))
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
    
    function handleParamChange(event, index) {
        const parameters = inputs.parameters;
        parameters[index] = {...parameters[index], [event.target.name]: event.target.value};
        setInputs(values => ({...values, parameters}))
    }

    return (
        <form className='calM' onSubmit={onSubmit}>
            <div className='MENU_SPRZED'>
                <div className="podstawa">
                    <Card className="podstawa" style={{ width: '18rem' }}>
                        <Card.Title id="ups" >
                        <Form.Control onChange={handleChange} name="name" type= "text" className="me-auto" placeholder={inputs.name!==""?inputs.name: "Nazwa produktu"} readOnly={true}/>
                        <Button href="#" variant="light" 
                            className={favorite ? "favorite active" : "favorite"} 
                            onClick={handleFavoriteClick}
                            >Dodaj do ulubionych 
                            <Heart isActive={favorite} onClick={handleFavoriteClick}/>
                        </Button>
                        </Card.Title>
                        <input name="photoFile" accept="image/png, image/jpeg" type="file" onChange={handleImageUpload}  required/>
                        <Card.Img variant="top" src={imageSrc} />
                        <Card.Body>
                            <div className="rating">
                                <p>Średnia ocena: <span>{rating.toFixed(2)}</span> z <span>{totalRatings}</span> ocen</p>
                                <div className="stars" data-rating={rating}>
                                    <span onClick={()=>handleRatingClick(1)} >&#9733;</span>
                                    <span onClick={()=>handleRatingClick(2)} >&#9733;</span>
                                    <span onClick={()=>handleRatingClick(3)} >&#9733;</span>
                                    <span onClick={()=>handleRatingClick(4)} >&#9733;</span>
                                    <span onClick={()=>handleRatingClick(5)} >&#9733;</span>
                                </div>
                            </div>
                        </Card.Body>
                    </Card>
                </div>
                <div className="produkt">
                    <Card >
                        <Card.Body>
                            <Card.Title >
                                <Form.Control id="alignment" onChange={handleChange} name="name" type= "text" className="me-auto" placeholder={"Nazwa produktu"} required/>
                            </Card.Title>
                            <Card.Subtitle className="mb-2 text-muted">
                                <Form.Control id="alignment" onChange={handleChange} name="price" type= "number" className="me-auto" placeholder={"Cena produktu"} required/>
                            </Card.Subtitle>
                            <Card.Subtitle className="mb-2 text-muted">
                                Kategoria
                                <Form.Select  name="categoryId" onChange={handleChange} required>
                                    <option value={null} hidden>wybierz</option>
                                    {category.length? 
                                        category.map((category) => (
                                            <option key={category.id} value={category.id}>{category.name}</option>
                                        ))
                                        :
                                        <option >"kategorie można dodać tylko na adminie"</option>
                                        
                                    }
                                </Form.Select>
                            </Card.Subtitle>
                            <Card.Subtitle className="mb-2 text-muted">
                                VAT
                                <Form.Select name="vat" onChange={handleChange}>
                                    <option value={null} hidden>wybierz</option>
                                    <option value={null} >0.23</option>
                                    <option value={null} >0.08</option>
                                    <option value={null} >0.05</option>
                                    <option value={null} >0.00</option>
                                </Form.Select>
                            </Card.Subtitle>
                        </Card.Body>
                        <ListGroup className='num' variant="flush">
                            <Button variant="primary">+</Button>
                            <Form.Control onChange={handleChange} name="number" type= "number" placeholder={ilość!==0?ilość:"Podaj"} aria-label="Example text with button addon" aria-describedby="basic-addon1" />
                            <Button variant="primary">-</Button>
                        </ListGroup>
                        <ListGroup  variant="flush">
                            <Button variant="light">Dodaj do koszyka</Button>
                            <Button variant="light">Kupuje i płacę</Button>
                        </ListGroup>
                    </Card>
                </div>
            </div>
            <div className='dodajParametr'>
                <FloatingLabel controlId="floatingTextarea2" label="Opis produktu: ">
                    <Form.Control onChange={handleChange} name="description" type= "text"
                    as="textarea"
                    placeholder="Leave a comment here"
                    style={{ height: '100px' }} 
                    required/>
                </FloatingLabel>
                <Card id="para1" style={{ width: '18rem' }}>
                    <Button  onClick={dodajParametr} variant="light">dodaj parametr</Button>
                </Card>
            </div>
            <div id="koks"><Card id="para3" style={{ width: '18rem' }}>
                <Card.Title>Parametry</Card.Title>
                {
                    inputs.parameters.map((x,i)=>{
                        return(
                            <Card key={x+i} id="para2" style={{ width: '18rem' }}>
                                <ListGroup key={i+"name"} id="impu" variant="flush">
                                    <Form.Control onChange={(e) => handleParamChange(e, i)} name="name" className="me-auto" placeholder="dodaj nazwe parametru...Stan" required/>
                                </ListGroup>
                                <ListGroup key={i+"value"} id="impu" variant="flush">                                
                                    <Form.Control onChange={(e) => handleParamChange(e, i)} name="value" className="me-auto" placeholder="dodaj ceche parametru...Nowy" required/>
                                </ListGroup>
                            </Card>
                        )
                    })
                }
                </Card>
            </div>
            <Button id="congirm" type='submit' variant="light">Zatwierdz produkt</Button>
        </form>
    )
}