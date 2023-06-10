import { Wrap, WrapItem, Spinner, Text } from '@chakra-ui/react'
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import { useEffect, useState } from "react";
import { getCustomers } from "./services/client.js";
import CardWithImage from "./components/Card.jsx";
import DrawerForm from "./components/DrawerForm.jsx";
import {errorNotification} from "./services/notification.js";

const App = () => {

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    const fetchCustomers = () => {
        setLoading(true);
        getCustomers().then(res => {
            setCustomers(res.data)
        }).catch(err => {
            errorNotification(
                err.code,
                err.message
            )
        }).finally( () => {
            setLoading(false)
        })
    }

    useEffect(() => {
        fetchCustomers()
    }, [])

    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }

    if(customers.length <= 0)  {
        return (
            <SidebarWithHeader>
                <DrawerForm
                    fetchCustomers={fetchCustomers}
                />
                <Text mt={5}>No customers available</Text>
            </SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            <DrawerForm
                fetchCustomers={fetchCustomers}
            />
            <Wrap justify = {"center"} spacing={"30px"}>
                {customers.map((customer, index) => (
                    <WrapItem key = {index}>
                        <CardWithImage
                            {...customer}
                            imageNumber={index}
                            fetchCustomers={fetchCustomers}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App;