import './Ulubione.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useState, useEffect } from 'react';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Dropdown from 'react-bootstrap/Dropdown';
import Stack from 'react-bootstrap/Stack';
import ListGroup from 'react-bootstrap/ListGroup';
import { useGet } from '../../hooks/useGet';
import { useFavoriteProduct } from '../../hooks/useFavoriteProduct';
import { post, put, remove } from '../../utils/Api';
import { useCategories } from '../../hooks/useCategories';


export function Ulubione() {
    const [isChecked, setIsChecked] = useState([]);
    const {category, errorCategory, loadingCategory} = useCategories();
    const {favoriteProduct, updateFavoriteProduct, errorFavoriteProduct, loadingFavoriteProduct, removeFavoriteProducts} = useFavoriteProduct();
    const [filterFavorite, setFilterFavorite] = useState({});

    async function removeFavoriteProduct(){
        const toRemove =[];
        isChecked.forEach((item,index)=>
            {
                if(item && favoriteProduct[index]?.id){
                    toRemove.push(favoriteProduct[index]?.id)
                }
            }
        )
        console.log(`order`,{ids:toRemove.join(',')})
        const res = await remove(`order`,{ids:toRemove.join(',')});
        console.log(res)
        removeFavoriteProducts(toRemove);
    }

    function handleChange(event){
        const name = event.target.name;
        const value = event.target.value;
        setFilterFavorite(values => ({...values, [name]: value}))
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

    useEffect(() => {
        if (!loadingFavoriteProduct) {
          setIsChecked(Array(favoriteProduct.length+1).fill(false));
        }
    }, [loadingFavoriteProduct, favoriteProduct]);

    
    return (
        <div className='ulubione'>
            <header>
                <Card >
                    <Stack id="Wcard"direction="horizontal" gap={3}>
                        <Form.Control name="nameProduct" onChange={handleChange} className="me-auto" placeholder="Wyszukaj ofertę po nazwie produktu..." />
                        <ListGroup id="kat-sort" variant="flush">
                            {loadingCategory ? 'Ładowanie...' : (
                            <>
                                <Form.Select name="categoryId" onChange={handleChange}>
                                    <option value={''}>wybierz</option>
                                    {category.map((category) => (
                                        <option key={category.id} value={category.id}>{category.name}</option>
                                    ))}
                                </Form.Select>
                            </>
                            )}
                            <Form.Select name="dateCategory" onChange={handleChange}>
                                <option value={-1}>Najstarsze</option>
                                <option value={1}>Ostatnio dodane</option>
                            </Form.Select>
                        </ListGroup>
                        <div className="vr" />
                    </Stack>
                </Card>
            </header>
            <div id="Uprod">
                {loadingFavoriteProduct ? 'Ładowanie...' : (
                <>
                    <Card id="CHALL" style={{ width: '18rem' }}>
                        <label>
                            <input type="checkbox"
                                checked={isChecked[isChecked.length - 1] || false}
                                onChange={() => checkBOX(isChecked.length - 1)}>
                            </input>Zaznacz wszystkie
                        </label>
                        <Button onClick={removeFavoriteProduct} variant="outline-danger">Usuń zaznaczone</Button>
                    </Card>
                    {
                        favoriteProduct.length>0 ?
                        favoriteProduct.filter(p => !filterFavorite.categoryId || p.product.category.id == filterFavorite.categoryId)
                            .sort((a, b) => {
                                return filterFavorite.dateCategory * (new Date(a.createdAt) - new Date(b.createdAt))
                            })
                            .filter(p => !filterFavorite.nameProduct || p.product.name.includes(filterFavorite.nameProduct
                                ))
                                .map((favorite,index)=>
                                    <Card key={favorite.product.id} id="WUprod" style={{ width: '18rem' }}>
                                        <ListGroup id="kat-sort" variant="flush">
                                            <input type={'checkbox'} checked={isChecked[index] || false} onChange={()=> checkBOX(index)}></input>
                                            <Card.Title>{favorite.product.name}</Card.Title>
                                        </ListGroup>
                                        <ListGroup id="kat-sort" variant="flush">
                                            <Card.Img id="img" variant="top" src={`data:image/png;base64, ${favorite.product.photo}`} />
                                            <ListGroup variant="flush">
                                                <ListGroup.Item>cena: {favorite.product.price}</ListGroup.Item>
                                                <ListGroup.Item>od: {favorite.product.user.name}</ListGroup.Item>
                                                <Card.Text className='word-break-all max-3-lines'>{favorite.product.description}</Card.Text>
                                            </ListGroup>
                                            <Button variant="outline-danger">Dodaj do koszyka</Button>
                                        </ListGroup>
                                    </Card>
                                )
                            :
                            <Card  id="WUprod" style={{ width: '18rem' }}>
                                nie dodałeś jeszcze żadnych produktów
                            </Card>

                    }
                </>
                )}
            </div>
        </div>
    )
}