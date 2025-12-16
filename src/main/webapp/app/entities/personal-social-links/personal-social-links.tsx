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

import { getEntities } from './personal-social-links.reducer';

export const PersonalSocialLinks = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const personalSocialLinksList = useAppSelector(state => state.personalSocialLinks.entities);
  const loading = useAppSelector(state => state.personalSocialLinks.loading);
  const totalItems = useAppSelector(state => state.personalSocialLinks.totalItems);

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
      <h2 id="personal-social-links-heading" data-cy="PersonalSocialLinksHeading">
        <Translate contentKey="monsterdamChicksApp.personalSocialLinks.home.title">Personal Social Links</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="monsterdamChicksApp.personalSocialLinks.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/personal-social-links/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="monsterdamChicksApp.personalSocialLinks.home.createLabel">Create new Personal Social Links</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {personalSocialLinksList && personalSocialLinksList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="monsterdamChicksApp.personalSocialLinks.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('normalImageS3Key')}>
                  <Translate contentKey="monsterdamChicksApp.personalSocialLinks.normalImageS3Key">Normal Image S 3 Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('normalImageS3Key')} />
                </th>
                <th className="hand" onClick={sort('thumbnailIconS3Key')}>
                  <Translate contentKey="monsterdamChicksApp.personalSocialLinks.thumbnailIconS3Key">Thumbnail Icon S 3 Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('thumbnailIconS3Key')} />
                </th>
                <th className="hand" onClick={sort('socialLink')}>
                  <Translate contentKey="monsterdamChicksApp.personalSocialLinks.socialLink">Social Link</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('socialLink')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="monsterdamChicksApp.personalSocialLinks.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="monsterdamChicksApp.personalSocialLinks.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="monsterdamChicksApp.personalSocialLinks.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="monsterdamChicksApp.personalSocialLinks.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('deletedDate')}>
                  <Translate contentKey="monsterdamChicksApp.personalSocialLinks.deletedDate">Deleted Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedDate')} />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.personalSocialLinks.socialNetwork">Social Network</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.personalSocialLinks.userProfile">User Profile</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {personalSocialLinksList.map((personalSocialLinks, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/personal-social-links/${personalSocialLinks.id}`} color="link" size="sm">
                      {personalSocialLinks.id}
                    </Button>
                  </td>
                  <td>{personalSocialLinks.normalImageS3Key}</td>
                  <td>{personalSocialLinks.thumbnailIconS3Key}</td>
                  <td>{personalSocialLinks.socialLink}</td>
                  <td>
                    {personalSocialLinks.createdDate ? (
                      <TextFormat type="date" value={personalSocialLinks.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {personalSocialLinks.lastModifiedDate ? (
                      <TextFormat type="date" value={personalSocialLinks.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{personalSocialLinks.createdBy}</td>
                  <td>{personalSocialLinks.lastModifiedBy}</td>
                  <td>
                    {personalSocialLinks.deletedDate ? (
                      <TextFormat type="date" value={personalSocialLinks.deletedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {personalSocialLinks.socialNetwork ? (
                      <Link to={`/social-network/${personalSocialLinks.socialNetwork.id}`}>{personalSocialLinks.socialNetwork.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {personalSocialLinks.userProfile ? (
                      <Link to={`/user-profile/${personalSocialLinks.userProfile.id}`}>{personalSocialLinks.userProfile.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/personal-social-links/${personalSocialLinks.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/personal-social-links/${personalSocialLinks.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/personal-social-links/${personalSocialLinks.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="monsterdamChicksApp.personalSocialLinks.home.notFound">No Personal Social Links found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={personalSocialLinksList && personalSocialLinksList.length > 0 ? '' : 'd-none'}>
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

export default PersonalSocialLinks;
