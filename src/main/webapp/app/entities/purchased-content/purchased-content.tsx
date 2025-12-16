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

import { getEntities } from './purchased-content.reducer';

export const PurchasedContent = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const purchasedContentList = useAppSelector(state => state.purchasedContent.entities);
  const loading = useAppSelector(state => state.purchasedContent.loading);
  const totalItems = useAppSelector(state => state.purchasedContent.totalItems);

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
      <h2 id="purchased-content-heading" data-cy="PurchasedContentHeading">
        <Translate contentKey="monsterdamChicksApp.purchasedContent.home.title">Purchased Contents</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="monsterdamChicksApp.purchasedContent.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/purchased-content/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="monsterdamChicksApp.purchasedContent.home.createLabel">Create new Purchased Content</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {purchasedContentList && purchasedContentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="monsterdamChicksApp.purchasedContent.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('rating')}>
                  <Translate contentKey="monsterdamChicksApp.purchasedContent.rating">Rating</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('rating')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="monsterdamChicksApp.purchasedContent.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="monsterdamChicksApp.purchasedContent.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="monsterdamChicksApp.purchasedContent.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="monsterdamChicksApp.purchasedContent.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('deletedDate')}>
                  <Translate contentKey="monsterdamChicksApp.purchasedContent.deletedDate">Deleted Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedDate')} />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.purchasedContent.contentPackage">Content Package</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.purchasedContent.payment">Payment</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.purchasedContent.viewer">Viewer</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.purchasedContent.creator">Creator</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {purchasedContentList.map((purchasedContent, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/purchased-content/${purchasedContent.id}`} color="link" size="sm">
                      {purchasedContent.id}
                    </Button>
                  </td>
                  <td>{purchasedContent.rating}</td>
                  <td>
                    {purchasedContent.createdDate ? (
                      <TextFormat type="date" value={purchasedContent.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {purchasedContent.lastModifiedDate ? (
                      <TextFormat type="date" value={purchasedContent.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{purchasedContent.createdBy}</td>
                  <td>{purchasedContent.lastModifiedBy}</td>
                  <td>
                    {purchasedContent.deletedDate ? (
                      <TextFormat type="date" value={purchasedContent.deletedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {purchasedContent.contentPackage ? (
                      <Link to={`/content-package/${purchasedContent.contentPackage.id}`}>{purchasedContent.contentPackage.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {purchasedContent.payment ? (
                      <Link to={`/payment/${purchasedContent.payment.id}`}>{purchasedContent.payment.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {purchasedContent.viewer ? (
                      <Link to={`/user-lite/${purchasedContent.viewer.id}`}>{purchasedContent.viewer.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {purchasedContent.creator ? (
                      <Link to={`/user-lite/${purchasedContent.creator.id}`}>{purchasedContent.creator.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/purchased-content/${purchasedContent.id}`}
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
                        to={`/purchased-content/${purchasedContent.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/purchased-content/${purchasedContent.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="monsterdamChicksApp.purchasedContent.home.notFound">No Purchased Contents found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={purchasedContentList && purchasedContentList.length > 0 ? '' : 'd-none'}>
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

export default PurchasedContent;
