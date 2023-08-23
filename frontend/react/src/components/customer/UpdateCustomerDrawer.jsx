import {
    Button,
    Drawer, DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay, Input,
    useDisclosure
} from "@chakra-ui/react";
import CreateCustomerForm from "./CreateCustomerForm.jsx";
import UpdateCustomerForm from "./UpdateCustomerForm.jsx";

const AddIcon = () => "+";

const UpdateCustomerDrawer = ({id, name, email, age, gender, fetchCustomers}) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return(
        <>
            <Button
                //leftIcon={<AddIcon/>}
                onClick={onOpen}
                bg={'blue'}
                color={'white'}
                rounded={'full'}
                _hover={{
                    transform: 'translateY(-2px)',
                    boxShadow: 'lg'
                }}
                _focus={{
                    bg: 'green.500'
                }}
            >
                Update
            </Button>
            <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Update customer</DrawerHeader>

                    <DrawerBody>
                        <UpdateCustomerForm
                            id={id}
                            name={name}
                            email={email}
                            age={age}
                            gender={gender}
                            fetchCustomers={fetchCustomers}
                        />
                    </DrawerBody>

                    <DrawerFooter>
                        <Button
                            colorScheme={"teal"}
                            onClick={onClose}
                        >
                            Cancel
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </>
    )
}

export default UpdateCustomerDrawer;

