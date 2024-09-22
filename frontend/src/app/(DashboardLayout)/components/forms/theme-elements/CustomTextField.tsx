import React from 'react';
import { styled } from '@mui/material/styles';
import { TextField } from '@mui/material';

const CustomTextField = styled((props: any) => <TextField {...props} />)(({ theme }) => ({
  // Styling for webkit-based browsers
  '& .MuiOutlinedInput-input::-webkit-input-placeholder': {
    color: theme.palette.text.secondary,
    opacity: '0.8',
  },
  // Styling for Mozilla browsers
  '& .MuiOutlinedInput-input::-moz-placeholder': {
    color: theme.palette.text.secondary,
    opacity: '0.8',
  },
  // Styling for Internet Explorer/Edge
  '& .MuiOutlinedInput-input:-ms-input-placeholder': {
    color: theme.palette.text.secondary,
    opacity: '0.8',
  },
  // Styling for any disabled input fields
  '& .MuiOutlinedInput-input.Mui-disabled::-webkit-input-placeholder': {
    color: theme.palette.text.secondary,
    opacity: '1',
  },
  // Styling for the outline when the field is disabled
  '& .Mui-disabled .MuiOutlinedInput-notchedOutline': {
    borderColor: theme.palette.grey[200],
  },
  // Optional: Add hover or focus styling if required
  '& .MuiOutlinedInput-root:hover .MuiOutlinedInput-notchedOutline': {
    borderColor: theme.palette.primary.light,
  },
}));

export default CustomTextField;
