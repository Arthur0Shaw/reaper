import Link from "next/link";
import { styled } from "@mui/material";
import Image from "next/image";

// Styled Link component
const LinkStyled = styled(Link)(() => ({
  height: "100px", // Increase height
  width: "220px", // Increase width
  overflow: "hidden",
  display: "block",
  margin: "0 auto", // Center horizontally
}));

// Logo Component
const Logo = () => {
  return (
    <LinkStyled href="/">
      {/* Adjust the height and width of the image as well */}
      <Image
        src="/images/logos/logo.svg"
        alt="Pay Now"
        height={150} // Increase height of the image
        width={200} // Increase width of the image
        priority
      />
    </LinkStyled>
  );
};

export default Logo;
