import {
  IconAperture,
  IconCopy,
  IconLayoutDashboard,
  IconHistory,
  IconUserPlus,
  IconUsers,
  IconBuildingBank,
  IconSettings,
  IconArrowRight,
} from "@tabler/icons-react"; // Import appropriate icons

import { uniqueId } from "lodash";

const Menuitems = [
  {
    navlabel: true,
    subheader: "Home",
  },
  {
    id: uniqueId(),
    title: "Dashboard",
    icon: IconLayoutDashboard,
    href: "/dashboard",
  },
  {
    navlabel: true,
    subheader: "Reports",
  },
  {
    id: uniqueId(),
    title: "History",
    icon: IconHistory,
    href: "/reports",
  },
  {
    id: uniqueId(),
    title: "Login History",
    icon: IconArrowRight,
    href: "/login-history",
  },
  {
    navlabel: true,
    subheader: "Authentication",
  },
  {
    id: uniqueId(),
    title: "Register",
    icon: IconUserPlus,
    href: "/register",
  },
  {
    navlabel: true,
    subheader: "Merchant",
  },
  {
    id: uniqueId(),
    title: "Merchant List",
    icon: IconUsers,
    href: "/merchant-list",
  },
  {
    navlabel: true,
    subheader: "Bank Management",
  },
  {
    id: uniqueId(),
    title: "Bank Mapping",
    icon: IconBuildingBank,
    href: "/bank-mapping",
  },
  {
    id: uniqueId(),
    title: "Routing",
    icon: IconSettings,
    href: "/routing",
  },
];

export default Menuitems;
