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

import { getEntities } from './payment.reducer';

export const Payment = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const paymentList = useAppSelector(state => state.payment.entities);
  const loading = useAppSelector(state => state.payment.loading);
  const totalItems = useAppSelector(state => state.payment.totalItems);

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
      <h2 id="payment-heading" data-cy="PaymentHeading">
        <Translate contentKey="monsterdamChicksApp.payment.home.title">Payments</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="monsterdamChicksApp.payment.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/payment/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="monsterdamChicksApp.payment.home.createLabel">Create new Payment</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {paymentList && paymentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="monsterdamChicksApp.payment.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('amount')}>
                  <Translate contentKey="monsterdamChicksApp.payment.amount">Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amount')} />
                </th>
                <th className="hand" onClick={sort('currency')}>
                  <Translate contentKey="monsterdamChicksApp.payment.currency">Currency</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('currency')} />
                </th>
                <th className="hand" onClick={sort('paymentDate')}>
                  <Translate contentKey="monsterdamChicksApp.payment.paymentDate">Payment Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('paymentDate')} />
                </th>
                <th className="hand" onClick={sort('paymentStatus')}>
                  <Translate contentKey="monsterdamChicksApp.payment.paymentStatus">Payment Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('paymentStatus')} />
                </th>
                <th className="hand" onClick={sort('paymentReference')}>
                  <Translate contentKey="monsterdamChicksApp.payment.paymentReference">Payment Reference</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('paymentReference')} />
                </th>
                <th className="hand" onClick={sort('cloudTransactionId')}>
                  <Translate contentKey="monsterdamChicksApp.payment.cloudTransactionId">Cloud Transaction Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cloudTransactionId')} />
                </th>
                <th className="hand" onClick={sort('providerPaymentIntentId')}>
                  <Translate contentKey="monsterdamChicksApp.payment.providerPaymentIntentId">Provider Payment Intent Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('providerPaymentIntentId')} />
                </th>
                <th className="hand" onClick={sort('providerChargeId')}>
                  <Translate contentKey="monsterdamChicksApp.payment.providerChargeId">Provider Charge Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('providerChargeId')} />
                </th>
                <th className="hand" onClick={sort('providerCustomerId')}>
                  <Translate contentKey="monsterdamChicksApp.payment.providerCustomerId">Provider Customer Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('providerCustomerId')} />
                </th>
                <th className="hand" onClick={sort('providerPaymentMethodId')}>
                  <Translate contentKey="monsterdamChicksApp.payment.providerPaymentMethodId">Provider Payment Method Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('providerPaymentMethodId')} />
                </th>
                <th className="hand" onClick={sort('providerEventLastId')}>
                  <Translate contentKey="monsterdamChicksApp.payment.providerEventLastId">Provider Event Last Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('providerEventLastId')} />
                </th>
                <th className="hand" onClick={sort('countryCode')}>
                  <Translate contentKey="monsterdamChicksApp.payment.countryCode">Country Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('countryCode')} />
                </th>
                <th className="hand" onClick={sort('providerFeeAmount')}>
                  <Translate contentKey="monsterdamChicksApp.payment.providerFeeAmount">Provider Fee Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('providerFeeAmount')} />
                </th>
                <th className="hand" onClick={sort('platformFeeAmount')}>
                  <Translate contentKey="monsterdamChicksApp.payment.platformFeeAmount">Platform Fee Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('platformFeeAmount')} />
                </th>
                <th className="hand" onClick={sort('creatorNetAmount')}>
                  <Translate contentKey="monsterdamChicksApp.payment.creatorNetAmount">Creator Net Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('creatorNetAmount')} />
                </th>
                <th className="hand" onClick={sort('taxAmount')}>
                  <Translate contentKey="monsterdamChicksApp.payment.taxAmount">Tax Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('taxAmount')} />
                </th>
                <th className="hand" onClick={sort('authorizedDate')}>
                  <Translate contentKey="monsterdamChicksApp.payment.authorizedDate">Authorized Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('authorizedDate')} />
                </th>
                <th className="hand" onClick={sort('capturedDate')}>
                  <Translate contentKey="monsterdamChicksApp.payment.capturedDate">Captured Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('capturedDate')} />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.payment.method">Method</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.payment.provider">Provider</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.payment.viewer">Viewer</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {paymentList.map((payment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/payment/${payment.id}`} color="link" size="sm">
                      {payment.id}
                    </Button>
                  </td>
                  <td>{payment.amount}</td>
                  <td>{payment.currency}</td>
                  <td>{payment.paymentDate ? <TextFormat type="date" value={payment.paymentDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    <Translate contentKey={`monsterdamChicksApp.GenericStatus.${payment.paymentStatus}`} />
                  </td>
                  <td>{payment.paymentReference}</td>
                  <td>{payment.cloudTransactionId}</td>
                  <td>{payment.providerPaymentIntentId}</td>
                  <td>{payment.providerChargeId}</td>
                  <td>{payment.providerCustomerId}</td>
                  <td>{payment.providerPaymentMethodId}</td>
                  <td>{payment.providerEventLastId}</td>
                  <td>{payment.countryCode}</td>
                  <td>{payment.providerFeeAmount}</td>
                  <td>{payment.platformFeeAmount}</td>
                  <td>{payment.creatorNetAmount}</td>
                  <td>{payment.taxAmount}</td>
                  <td>
                    {payment.authorizedDate ? <TextFormat type="date" value={payment.authorizedDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{payment.capturedDate ? <TextFormat type="date" value={payment.capturedDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{payment.method ? <Link to={`/payment-method/${payment.method.id}`}>{payment.method.id}</Link> : ''}</td>
                  <td>{payment.provider ? <Link to={`/payment-provider/${payment.provider.id}`}>{payment.provider.id}</Link> : ''}</td>
                  <td>{payment.viewer ? <Link to={`/user-lite/${payment.viewer.id}`}>{payment.viewer.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/payment/${payment.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/payment/${payment.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/payment/${payment.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="monsterdamChicksApp.payment.home.notFound">No Payments found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={paymentList && paymentList.length > 0 ? '' : 'd-none'}>
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

export default Payment;
