import { useState, useEffect } from 'react'
import {useParams} from "react-router-dom";
import axios from 'axios'
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Hazards from './Hazards'
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';

const Tasks = () => {
    const {title} = useParams();
    const [tasks, setTasks] = useState([]);
    const [step, setStep] = useState('');

    const makeNewStep = async () => {
        // eslint-disable-next-line no-restricted-globals
        event.preventDefault();
        const response = await axios.post(`http://localhost:8080/task/${title}`, null, {params: { step: step}});
        try {
            const addNewStep = tasks.concat(response.data);
            setTasks(addNewStep);
        } catch (err) {
            console.log("The response message is ", response.message)
        }
    }

    useEffect(() => {
        const getTasks = async () => {
            const response = axios.get(`http://localhost:8080/task/${title}`)
            try {
                const jhaTasks = (await response).data.map(task => {
                    return task;
                })
                setTasks(jhaTasks);
                setStep('');
            } catch (err){
                console.log("Error retrieving tasks", err);
            }
        }
        getTasks();
    }, [title]);

    return(
        <Container>
            <h5 className='py-1'>Job Hazard Analysis of {title}</h5>
                <Form onSubmit={makeNewStep}>
                    <Row>
                        <Col>
                            <Form.Group className="mb-3">
                                <Form.Control type="text" required={true} value={step} placeholder="Add New Task Here" onChange={(event) => setStep(event.target.value)}/>
                            </Form.Group>
                        </Col>
                        <Col>
                            <Button variant="primary" type="submit">
                                Submit
                            </Button>
                        </Col>
                    </Row>
                </Form>
            {tasks.map((task, i) => {
                return (
                    <Row className="flex-fill bg-light border">
                        <Col sm={2}>
                            <p><b>Step {(i + 1)}:</b> {task.step}</p>
                        </Col>
                        <Col>
                            <Hazards taskID={task.taskID}/>
                        </Col>
                    </Row>
                )
            })}
        </Container>
    )
}

export default Tasks;