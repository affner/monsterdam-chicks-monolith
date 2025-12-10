import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './user-profile.reducer';

export const UserProfile = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const userProfileList = useAppSelector(state => state.userProfile.entities);
  const loading = useAppSelector(state => state.userProfile.loading);
  const totalItems = useAppSelector(state => state.userProfile.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="user-profile-heading" data-cy="UserProfileHeading">
        <Translate contentKey="monsterdamChicksApp.userProfile.home.title">User Profiles</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="monsterdamChicksApp.userProfile.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-profile/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="monsterdamChicksApp.userProfile.home.createLabel">Create new User Profile</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userProfileList && userProfileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('emailContact')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.emailContact">Email Contact</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('emailContact')} />
                </th>
                <th className="hand" onClick={sort('profilePhotoS3Key')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.profilePhotoS3Key">Profile Photo S 3 Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('profilePhotoS3Key')} />
                </th>
                <th className="hand" onClick={sort('coverPhotoS3Key')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.coverPhotoS3Key">Cover Photo S 3 Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('coverPhotoS3Key')} />
                </th>
                <th className="hand" onClick={sort('mainContentUrl')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.mainContentUrl">Main Content Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mainContentUrl')} />
                </th>
                <th className="hand" onClick={sort('mobilePhone')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.mobilePhone">Mobile Phone</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mobilePhone')} />
                </th>
                <th className="hand" onClick={sort('websiteUrl')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.websiteUrl">Website Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('websiteUrl')} />
                </th>
                <th className="hand" onClick={sort('amazonWishlistUrl')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.amazonWishlistUrl">Amazon Wishlist Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amazonWishlistUrl')} />
                </th>
                <th className="hand" onClick={sort('lastLoginDate')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.lastLoginDate">Last Login Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastLoginDate')} />
                </th>
                <th className="hand" onClick={sort('biography')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.biography">Biography</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('biography')} />
                </th>
                <th className="hand" onClick={sort('isFree')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.isFree">Is Free</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isFree')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="monsterdamChicksApp.userProfile.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.userProfile.stateOfResidence">State Of Residence</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userProfileList.map((userProfile, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-profile/${userProfile.id}`} color="link" size="sm">
                      {userProfile.id}
                    </Button>
                  </td>
                  <td>{userProfile.emailContact}</td>
                  <td>{userProfile.profilePhotoS3Key}</td>
                  <td>{userProfile.coverPhotoS3Key}</td>
                  <td>{userProfile.mainContentUrl}</td>
                  <td>{userProfile.mobilePhone}</td>
                  <td>{userProfile.websiteUrl}</td>
                  <td>{userProfile.amazonWishlistUrl}</td>
                  <td>
                    {userProfile.lastLoginDate ? (
                      <TextFormat type="date" value={userProfile.lastLoginDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{userProfile.biography}</td>
                  <td>{userProfile.isFree ? 'true' : 'false'}</td>
                  <td>
                    {userProfile.createdDate ? <TextFormat type="date" value={userProfile.createdDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {userProfile.lastModifiedDate ? (
                      <TextFormat type="date" value={userProfile.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{userProfile.createdBy}</td>
                  <td>{userProfile.lastModifiedBy}</td>
                  <td>{userProfile.isDeleted ? 'true' : 'false'}</td>
                  <td>
                    {userProfile.stateOfResidence ? (
                      <Link to={`/state/${userProfile.stateOfResidence.id}`}>{userProfile.stateOfResidence.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-profile/${userProfile.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-profile/${userProfile.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/user-profile/${userProfile.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="monsterdamChicksApp.userProfile.home.notFound">No User Profiles found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={userProfileList && userProfileList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default UserProfile;
