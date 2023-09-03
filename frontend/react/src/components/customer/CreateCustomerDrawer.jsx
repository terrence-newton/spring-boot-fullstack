import {
    Button,
    Drawer, DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay, Input,
    useDisclosure
} from "@chakra-ui/react";
import CreateCustomerForm from "../shared/CreateCustomerForm.jsx";

const AddIcon = () => "+";

const CreateCustomerDrawer = ({fetchCustomers}) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return(
        <>
            <Button
                leftIcon={<AddIcon/>}
                colorScheme={"teal"}
                onClick={onOpen}
            >
                Create customer
            </Button>
            <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Create new customer</DrawerHeader>

                    <DrawerBody>
                        <CreateCustomerForm
                            onSuccess={fetchCustomers}
                        />
                    </DrawerBody>

                    <DrawerFooter>
                        <Button
                            //leftIcon={<CloseIcon/>}
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

export default CreateCustomerDrawer;