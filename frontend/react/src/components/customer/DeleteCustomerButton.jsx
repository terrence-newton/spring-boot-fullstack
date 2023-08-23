import React from 'react'
import {
    AlertDialog,
    AlertDialogBody,
    AlertDialogCloseButton, AlertDialogContent, AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogOverlay,
    Button, useDisclosure
} from "@chakra-ui/react";
import {deleteCustomer} from "../../services/client.js";
import {errorNotification, successNotification} from "../../services/notification.js";

export default function DeleteCustomerButton({id, fetchCustomers}) {
    const { isOpen, onOpen, onClose } = useDisclosure()
    const cancelRef = React.useRef()

    return (
        <>
            <Button
                onClick={onOpen}
                bg={'red.400'}
                color={'white'}
                rounded={'full'}
                _hover={{
                    transform: 'translateY(-2px)',
                    boxShadow: 'lg'
                }}
                _focus={{
                    bg: 'green.500'
                }}
            >Delete</Button>
            <AlertDialog
                motionPreset='slideInBottom'
                leastDestructiveRef={cancelRef}
                onClose={onClose}
                isOpen={isOpen}
                isCentered
            >
                <AlertDialogOverlay />

                <AlertDialogContent>
                    <AlertDialogHeader>Discard Changes?</AlertDialogHeader>
                    <AlertDialogCloseButton />
                    <AlertDialogBody>
                        Are you sure you want to delete this customer?
                    </AlertDialogBody>
                    <AlertDialogFooter>
                        <Button ref={cancelRef} onClick={onClose}>
                            No
                        </Button>
                        <Button colorScheme='red' ml={3}
                                onClick={() => {
                                    deleteCustomer(id)
                                        .then( res => {
                                            successNotification("Deleted","Deleted");
                                            fetchCustomers();
                                            }
                                        ).catch(err => {
                                        errorNotification(
                                            err.code,
                                            err.response.data.message
                                        )
                                    }).finally(onClose)
                                }}
                        >
                            Yes
                        </Button>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </>
    )
}