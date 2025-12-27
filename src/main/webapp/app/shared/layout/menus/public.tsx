import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import MenuItem from 'app/shared/layout/menus/menu-item';
import { usePublicNavigation } from 'app/shared/hooks/use-public-navigation';
import { NavDropdown } from './menu-components';

export const PublicMenu = () => {
  const { items, loading } = usePublicNavigation();

  if (!items.length && !loading) {
    return null;
  }

  return (
    <NavDropdown icon="compass" name="Navegación" id="public-menu" data-cy="publicMenu">
      {loading && <DropdownItem disabled>Cargando...</DropdownItem>}
      {items.map(item =>
        item.path.startsWith('http') ? (
          <DropdownItem key={item.id ?? item.path} tag="a" href={item.path} target="_blank" rel="noopener noreferrer">
            <FontAwesomeIcon icon="road" fixedWidth /> {item.label}
          </DropdownItem>
        ) : (
          <MenuItem key={item.id ?? item.path} icon="road" to={item.path}>
            {item.label}
          </MenuItem>
        ),
      )}
    </NavDropdown>
  );
};
