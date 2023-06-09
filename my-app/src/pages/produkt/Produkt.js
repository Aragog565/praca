import './Produkt.css';
import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Heart from "react-heart"
import ListGroup from 'react-bootstrap/ListGroup';
import Form from 'react-bootstrap/Form';
import { useParams } from 'react-router-dom';
import { useRecentProducts } from '../../hooks/useRecentProducts';
import { post, put, remove } from '../../utils/Api';
import { useComments } from '../../hooks/useComments';
import { useProduct } from '../../hooks/useProduct';
import Alert from 'react-bootstrap/Alert';
import { useAuth } from '../../hooks/use-auth';
import { environment } from '../../environment';
import { useBasket } from '../../hooks/useBasket';




export function Produkt() {
    const {id} = useParams();
    const {addProduct} = useRecentProducts();
    const {product, updateProduct, errorProduct, loadingProduct} = useProduct(id);
    const {comments, addComment, errorComment, loadingComment} = useComments(id);
    const [inputs, setInputs] = useState({});
    const handleChange = (event) => {
        const name = event.target.name;
        const value = event.target.value;
        setInputs(values => ({...values, [name]: value}))
      }
    const [number, setNumber] = useState(1);
    const [show, setShow] = useState(false);
    const [editedComment, setEditedComment] = useState(null);
    const [auth, setAuth] = useAuth();
    const {addProductToBasket,isProductInBasket , removeProductBasket} = useBasket();
    

    async function onSubmit(event){
        event.preventDefault();
        try{
            const res = await post(`products/${id}/comments`, inputs);
            addComment(res.data);
        }catch(e){
            setShow(true)
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }
    }

    function handleNumber(nr){
        if(number+nr<0){
            return setNumber(0);
        }
        setNumber((prev) => prev+nr);
    }
    function handleNumberSet(nr){
        setNumber(+nr);
    }
    async function handleRatingClick(event){
        try{
            const res = await post(`products/${id}/rate`, {value: event});
            updateProduct(res.data.product)
        }catch(e){
            setShow(true);
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }
    }

    async function handleFavoriteClick() {
        if(!product.isFavorite){
            try{
                const res = await post(`products/${id}/favorite`);
                res.data.product.isFavorite=true;
                updateProduct(res.data.product)
            }catch(e){
                setShow(true);
                window.scrollTo({ top: 0, behavior: 'smooth' });
            }
        }else{
            try{
                const res = await remove(`products/${id}/favorite`);
                updateProduct(prev=>({...prev,isFavorite:false}));                
            }catch(e){
                setShow(true);
                window.scrollTo({ top: 0, behavior: 'smooth' });
            }
        }
    };

    async function onEditComment(event){
        event.preventDefault();
        try{
            const res = await put(`products/${id}/comments/${editedComment.id}`, editedComment);
            setEditedComment(null);
            addComment(editedComment);
        }catch(e){
            setShow(true);
        }
    }

    function changeBasket(){
        if(!isProductInBasket(product.id)){
            addProductToBasket(product.id,number)
        }else{
            removeProductBasket(product.id)
        }
    }

      
    useEffect(() => {
        addProduct(id)
    }, [id, addProduct]);

    return (
        <div className='calMy'>
            <Alert id="alertR" show={show} variant="success">
                <Alert.Heading>Logowanie</Alert.Heading>
                <p>
                    Zaloguj się.
                </p>
                <hr />
                <div className="d-flex justify-content-end ">
                    <Button onClick={() => setShow(false)} variant="outline-success">
                        zamknij
                    </Button>
                    <Button href="/logowanie" onClick={() => setShow(false)} variant="outline-success">
                        Do logowania
                    </Button>
                </div>
          </Alert>
            {loadingProduct ? 'Ładowanie...' : (
            <>
                <div className='MENU'>
                    <div className="podstawa2">
                        <Card className="podstawa2" style={{ width: '18rem' }}>
                            <Card.Title id="ups"> {product.name}
                                <Button onClick={handleFavoriteClick} variant="light" 
                                    className={product.isFavorite ? "favorite active" : "favorite"} 
                                    >Dodaj do ulubionych 
                                    <Heart onClick={()=>{}} isActive={product.isFavorite} />
                                </Button>
                            </Card.Title>
                            <Card.Img variant="top" src={`data:image/png;base64, ${product.photo}`} />
                            <Card.Body>
                                <div className="rating">
                                    <p>Średnia ocena:
                                        <span>{(product.avgRate.toFixed(2))}</span> z
                                        <span>{product.totalRates}</span> ocen
                                    </p>
                                    <div className="stars" >
                                        {Array(5).fill().map((v, i) => (
                                            <span key={i+"star"} 
                                                name="value"
                                                className={i < Math.round(product.avgRate) ? 'highlighted-star' : ''} 
                                                onClick={() => handleRatingClick(i+1)} >&#9733;
                                            </span>
                                        ))}{(product.avgRate.toFixed(2))}
                                        {/* <span onClick={()=>handleRatingClick(2)} >&#9733;</span>
                                        <span onClick={()=>handleRatingClick(3)} >&#9733;</span>
                                        <span onClick={()=>handleRatingClick(4)} >&#9733;</span>
                                        <span onClick={()=>handleRatingClick(5)} >&#9733;</span> */}
                                    </div>
                                </div>
                            </Card.Body>
                        </Card>
                    </div>
                    <div className="produkt">
                        <Card style={{ width: '50%' }}>
                            <Card.Body>
                                <Card.Title>Produkt: {product.name} </Card.Title>
                                <Card.Title>Urzytkownik: {product.user.name}</Card.Title>
                                <Card.Subtitle className="mb-2 text-muted">Cena: {product.price}</Card.Subtitle>
                                <Card.Subtitle className="mb-2 text-muted">Ilość: {product.number}</Card.Subtitle>
                                <Card.Text>

                                </Card.Text>
                            </Card.Body>
                            <ListGroup className='num' variant="flush">
                                <Button onClick={() => handleNumber(-1)} variant="primary">-</Button>
                                <Form.Control value={number} onChange={(e)=>handleNumberSet(e.target.value)} placeholder='0' aria-label="Example text with button addon" aria-describedby="basic-addon1"/>
                                <Button onClick={()=>handleNumber(+1)} variant="primary">+</Button>
                            </ListGroup>
                            <ListGroup  variant="flush">
                                <Button onClick={changeBasket} variant="light">{isProductInBasket(product.id) ?"Usuń z koszyka":"Dodaj do koszyka"}</Button>
                                <Button href='/Koszyk' variant="light">Kupuje i płacę</Button>
                            </ListGroup>
                        </Card>
                    </div>
                </div>
                <div className='parametr'>
                    <Card>
                        <Card.Title>Opis: </Card.Title>
                        <Card.Body>{product.description}</Card.Body>
                    </Card>
                    <Card id="para" style={{ width: '18rem' }}>
                        <Card.Title>Parametry</Card.Title>
                            {product.parameters.map((param)=>
                                <ListGroup key={param.id+"parameter"} id="test" variant="flush">
                                    <ListGroup.Item>{param.name}</ListGroup.Item>
                                    <ListGroup.Item>{param.value}</ListGroup.Item>
                                </ListGroup>
                            )}
                    </Card>
                </div>
                <div className='newOpinions'>
                    <Form onSubmit={onSubmit} id="zOpinie" className='zOpinie'>
                        <Form.Group id="wnf" className="mb-3" controlId="exampleForm.ControlTextarea1">
                            <Form.Label>Zostaw swoją opinie</Form.Label>
                            <Form.Control name="content" onChange={handleChange} as="textarea" rows={3} />
                            <Button type='submit' variant="light">Wyślij</Button>
                        </Form.Group>
                    </Form>
                </div>
            </>
            )}
            <div  className='newOpinions'>
                {loadingComment ? 'Ładowanie...' : (
                <>
                    {
                        comments.map((comment)=>
                            <ListGroup id="zOpinie" className='zOpinie' as="ul">
                                <div className='img-wrapper'>
                                    <Card.Img id="comentPhoto" variant="top" src={comment.user.photo ? `data:image/png;base64, ${comment.user.photo}`: "/assets/user.jpg"} />
                                </div>
                                <ListGroup.Item id="comment" key={comment.id+"comment"} as="li">
                                    <div className="fw-bold">{comment.user.name+" "+comment.user.surname}</div>
                                    {editedComment?.id===comment.id?
                                    <Form onSubmit={onEditComment}  id="zOpinie" className='zOpinie'>
                                        <Form.Group id="wnf" className="mb-3" controlId="exampleForm.ControlTextarea1">
                                            <Form.Control onChange={(e) => setEditedComment(prev=>({...prev,content:e.target.value}))} value={editedComment.content} name="content"  as="textarea" rows={3} />
                                            <Button type='submit' variant="light">Wyślij</Button>
                                        </Form.Group>
                                    </Form>
                                    :
                                    <div className='content'>
                                        {comment.content}
                                        {
                                            comment.user.id==auth?.id || 
                                            auth?.role.name==environment.ROLE.ADMIN ? 
                                            <Button onClick={()=>setEditedComment(comment)} variant="light">edytuj</Button>:null
                                        }
                                    </div>
                                    }
                                </ListGroup.Item>
                            </ListGroup>
                        )
                    }
                </>
                )}
            </div>
        </div>
    )
}