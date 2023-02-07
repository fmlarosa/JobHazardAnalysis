import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import { Link } from 'react-router-dom';

const DisplayJha = (props) => {

    
    return(
        <Container className="d-flex justify-content-between pb-3" >
            <Row className="flex-fill bg-light border" >
                <Col sm={5}>Title: {props.title}</Col>
                <Col sm={5}>Written By: {props.author}</Col>
                <Col sm={1}className='d-md-block d-sm-none'><Link to={`/editJha/${props.title}`} className="btn btn-secondary"><i class="fas fa-regular fa-file-pen"></i></Link></Col>
                <Col sm={1}className='d-md-block d-sm-none'><Button onClick={props.deleteJha} variant="danger"><i class="fas fa-regular fa-trash"></i></Button></Col>
            </Row>
        </Container>
    )
}

export default DisplayJha;