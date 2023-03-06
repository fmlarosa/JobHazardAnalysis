import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import { LinkContainer } from 'react-router-bootstrap';

const Header = () => {
    return (
      <Navbar bg="dark" variant='dark' expand="lg">
        <Container>
          <Navbar.Brand>Welcome to Acme Job Hazard Analysis!</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="ms-auto">
                <LinkContainer to='/'>
                  <Nav.Link><i class="fas fa-duotone fa-house fa-2x"></i> Home</Nav.Link>
                </LinkContainer>
                <LinkContainer to='/createJha'>
                    <Nav.Link><i class="fas fa-square-plus fa-2x"></i> Add JHA</Nav.Link>
                </LinkContainer>
              </Nav>
            </Navbar.Collapse>
          </Container>
    </Navbar>
    )
}

export default Header;