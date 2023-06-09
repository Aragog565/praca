import { Outlet, Link, useNavigate } from "react-router-dom";
import { Footer } from "../footer/Footer";
import './Layout.css'

import 'bootstrap/dist/css/bootstrap.min.css';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Nav from 'react-bootstrap/Nav';
import Dropdown from 'react-bootstrap/Dropdown';
import Card from 'react-bootstrap/Card';
import ListGroup from 'react-bootstrap/ListGroup';
import Toast from 'react-bootstrap/Toast';
import { useAuth } from "../../hooks/use-auth";
import { useCategories } from "../../hooks/useCategories";


export function Layout(){
  const [auth, setAuth] = useAuth();
  const {category, errorCategory, loadingCategory} = useCategories();
  const navigate = useNavigate();

  function getAuthLabel(){
    if(auth){
      return false
    }
    return true
  }

  function onAuthChange(){
    if (auth){
      setAuth(null)
    }
    navigate("/logowanie")

  }
console.log(auth)
  return (

    <>
      <div className="base">
        <nav>
          <Link to="/" ><img src="/assets/logo.svg"></img></Link>
          {loadingCategory ? 'Ładowanie...' : (
            <>
              <DropdownButton  variant="" drop='end' id="dropdown-basic-button" title="Kategorie">
                {category.map((category) => (
                  <Dropdown.Item key={category.id} href={`/SearchProduct?categoryId=${category.id}`}>{category.name}</Dropdown.Item>
                ))}
              </DropdownButton>
            </>
          )}
          <Nav.Link className='but' href="/Koszyk">Koszyk</Nav.Link>
          <Nav.Link className='but' href="/Promocje">Promocje</Nav.Link>
          {getAuthLabel() ? null :<Nav.Link className='but' href="/Ulubione">Ulubione</Nav.Link>}
          {getAuthLabel() ? null :<Nav.Link className='but' href="/Sprzedaj">Sprzedaj</Nav.Link>}
          { auth?(
            
            <DropdownButton  variant="" drop='end' id="dropdown-basic-button" title="Konto">
              <Card style={{ width: '18rem' }}>
                <Card.Img variant="top" src={auth.photo ? `data:image/png;base64, ${auth.photo}` : "/assets/user.jpg"} />
                <Dropdown.Item disabled>{auth.name}</Dropdown.Item>
                <Dropdown.Item disabled>{auth.surname}</Dropdown.Item>
                <Dropdown.Item disabled>{auth.email}</Dropdown.Item>
                <ListGroup className="list-group-flush">
                  <Dropdown.Item href="/Konto">Moje konto</Dropdown.Item>
                </ListGroup>
              </Card>
              <Dropdown.Item href="/historiaTransakcji">Lista zamówień</Dropdown.Item>
              <Dropdown.Item href="/MyProduct">Twoje produkty</Dropdown.Item>
            </DropdownButton>
            ):null
          }
          <Nav.Link className='but' onClick={onAuthChange}>{getAuthLabel() ? 'Zaloguj' : 'Wyloguj'}</Nav.Link>
          
          {auth.role.id!=1 ? null :<Nav.Link className='but' href="/ForAdmin">Panel ADMINA</Nav.Link>}
          <Nav.Link className='margin' ></Nav.Link>
        </nav>
        <Outlet />
      </div>
      <Footer></Footer>
    </>
  )
};