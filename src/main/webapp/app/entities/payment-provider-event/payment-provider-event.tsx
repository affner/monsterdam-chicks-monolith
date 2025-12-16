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

import { getEntities } from './payment-provider-event.reducer';

export const PaymentProviderEvent = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const paymentProviderEventList = useAppSelector(state => state.paymentProviderEvent.entities);
  const loading = useAppSelector(state => state.paymentProviderEvent.loading);
  const totalItems = useAppSelector(state => state.paymentProviderEvent.totalItems);

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
      <h2 id="payment-provider-event-heading" data-cy="PaymentProviderEventHeading">
        <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.home.title">Payment Provider Events</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/payment-provider-event/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.home.createLabel">Create new Payment Provider Event</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {paymentProviderEventList && paymentProviderEventList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('providerName')}>
                  <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.providerName">Provider Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('providerName')} />
                </th>
                <th className="hand" onClick={sort('eventType')}>
                  <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.eventType">Event Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('eventType')} />
                </th>
                <th className="hand" onClick={sort('eventId')}>
                  <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.eventId">Event Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('eventId')} />
                </th>
                <th className="hand" onClick={sort('payloadJson')}>
                  <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.payloadJson">Payload Json</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('payloadJson')} />
                </th>
                <th className="hand" onClick={sort('receivedAt')}>
                  <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.receivedAt">Received At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('receivedAt')} />
                </th>
                <th className="hand" onClick={sort('processedAt')}>
                  <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.processedAt">Processed At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('processedAt')} />
                </th>
                <th className="hand" onClick={sort('processingStatus')}>
                  <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.processingStatus">Processing Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('processingStatus')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('deletedDate')}>
                  <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.deletedDate">Deleted Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedDate')} />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.payment">Payment</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {paymentProviderEventList.map((paymentProviderEvent, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/payment-provider-event/${paymentProviderEvent.id}`} color="link" size="sm">
                      {paymentProviderEvent.id}
                    </Button>
                  </td>
                  <td>{paymentProviderEvent.providerName}</td>
                  <td>{paymentProviderEvent.eventType}</td>
                  <td>{paymentProviderEvent.eventId}</td>
                  <td>{paymentProviderEvent.payloadJson}</td>
                  <td>
                    {paymentProviderEvent.receivedAt ? (
                      <TextFormat type="date" value={paymentProviderEvent.receivedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {paymentProviderEvent.processedAt ? (
                      <TextFormat type="date" value={paymentProviderEvent.processedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    <Translate contentKey={`monsterdamChicksApp.GenericStatus.${paymentProviderEvent.processingStatus}`} />
                  </td>
                  <td>{paymentProviderEvent.createdBy}</td>
                  <td>{paymentProviderEvent.lastModifiedBy}</td>
                  <td>
                    {paymentProviderEvent.deletedDate ? (
                      <TextFormat type="date" value={paymentProviderEvent.deletedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {paymentProviderEvent.payment ? (
                      <Link to={`/payment/${paymentProviderEvent.payment.id}`}>{paymentProviderEvent.payment.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/payment-provider-event/${paymentProviderEvent.id}`}
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
                        to={`/payment-provider-event/${paymentProviderEvent.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/payment-provider-event/${paymentProviderEvent.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.home.notFound">No Payment Provider Events found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={paymentProviderEventList && paymentProviderEventList.length > 0 ? '' : 'd-none'}>
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

export default PaymentProviderEvent;
