import { useState, useEffect } from 'react'
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import axios from 'axios'
import SafeGuards from './SafeGuards';
import Form from 'react-bootstrap/Form';
import Container from 'react-bootstrap/Container';


const Hazards = (props) => {
    const [hazards, setHazards] = useState([]);
    const [newHazard, setNewHazard] = useState('');

    const makeNewHazard = async (taskID) => {
        // eslint-disable-next-line no-restricted-globals
        event.preventDefault();
        const response = await axios.post(`http://localhost:8080/hazard/${props.taskID}`, null, {params: { danger: newHazard}});
        try {
            const addNewHazard = hazards.concat(response.data);
            setHazards(addNewHazard);
        } catch (err) {
            console.log("The response message is ", response.message)
        }
    }

    useEffect(() => {
        const getHazards = async () => {
            const response = axios.get(`http://localhost:8080/hazard/${props.taskID}`)
            try {
                const jhaHazards = (await response).data.map(task => {
                    return task;
                })
                setHazards(jhaHazards);
            } catch (err){
                console.log("Error retrieving hazards", err);
            }
        }
        getHazards();
    },[]);

    return(
        <Container>
            <Row>
                <Form onSubmit={makeNewHazard}>
                    <Row>
                        <Col>
                            <Form.Group className="mb-3">
                                <Form.Control type="text" required={true} value={newHazard} placeholder="Enter New Hazard" onChange={(event) => setNewHazard(event.target.value)}/>
                            </Form.Group>
                        </Col>
                        <Col>
                            <Button variant="primary" type="submit">
                                Submit
                            </Button>
                        </Col>
                    </Row>
                </Form>
            </Row>
            <Row className="d-flex flex-wrap">
                {hazards.map((hazard) => {
                    return  (<Col sm={3}>
                                <p className="mb-0"><b>Hazard:</b></p>{hazard.danger}<SafeGuards hazardID={hazard.hazardID}></SafeGuards>
                            </Col>)
                    })}
            </Row>
        </Container>
    )
}

export default Hazards;
